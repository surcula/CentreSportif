<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style>
    .autocomplete-menu{
        position:absolute; z-index:1000; top:100%; left:0; right:0;
        background:#fff; border:1px solid #ddd; border-radius:.35rem; margin-top:2px;
        max-height:220px; overflow:auto; display:none;
    }
    .autocomplete-item{ padding:.5rem .75rem; cursor:pointer; }
    .autocomplete-item:hover,.autocomplete-item.active{ background:#f2f4f7; }
</style>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<section class="page-section">
    <h2 class="page-section-heading text-center text-uppercase text-secondary mb-0">
        Abonnement — Formulaire
    </h2>

    <c:if test="${not empty error}">
        <div class="alert alert-danger text-center">${error}</div>
    </c:if>

    <c:if test="${not empty errors}">
        <div class="alert alert-danger">
            <ul class="mb-0">
                <c:forEach var="e" items="${errors}">
                    <li><strong>${e.key} :</strong> ${e.value}</li>
                </c:forEach>
            </ul>
        </div>
    </c:if>

    <!-- Form -->
    <div class="card my-4">
        <div class="card-body">

            <c:choose>
                <c:when test="${not empty usersSubscription.id}">
                    <form method="post" action="${ctx}/subscription">
                        <input type="hidden" name="action" value="update"/>
                        <input type="hidden" name="usersSubscriptionId" value="${usersSubscription.id}"/>

                        <div class="mb-3">
                            <label class="form-label">Date de début</label>
                            <input type="date" name="startDate" class="form-control"
                                   value="${usersSubscription.startDate}"/>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Date de fin</label>
                            <input type="date" name="endDate" class="form-control"
                                   value="${usersSubscription.endDate}"/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Quantité</label>
                            <input type="number" name="quantity" class="form-control" min="0"
                                   value="${usersSubscription.quantityMax}"/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Actif</label>
                            <select name="active" class="form-select">
                                <option value="1" <c:if test="${usersSubscription.active}">selected</c:if>>Oui</option>
                                <option value="0" <c:if test="${!usersSubscription.active}">selected</c:if>>Non</option>
                            </select>
                        </div>

                        <div class="text-end">
                            <button class="btn btn-primary" type="submit">Mettre à jour</button>
                            <a class="btn btn-secondary" href="${ctx}/subscription">Annuler</a>
                        </div>
                    </form>
                </c:when>


                <c:otherwise>
                    <form method="post" action="${ctx}/subscription">
                        <input type="hidden" name="action" value="assign"/>

                        <div class="mb-3 position-relative">
                            <label class="form-label">Utilisateur</label>
                            <input type="text" id="userSearch" class="form-control" placeholder="Tape nom/prénom/email…" autocomplete="off">
                            <input type="hidden" name="userId" id="userId"> <!-- c'est cet ID qui part -->
                            <div id="userResults" class="autocomplete-menu"></div>
                        </div>

                        <div class="mb-3 position-relative">
                            <label class="form-label">Abonnement</label>
                            <input type="text" id="subSearch" class="form-control" placeholder="Tape le nom de l’abonnement…" autocomplete="off">
                            <input type="hidden" name="subscriptionId" id="subscriptionId">
                            <div id="subResults" class="autocomplete-menu"></div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Date de début</label>
                            <input type="date" name="startDate" class="form-control"
                                   value="${param.startDate}"/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Date de fin</label>
                            <input type="date" name="endDate" class="form-control"
                                   value="${param.endDate}"/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Quantité</label>
                            <input type="number" name="quantity" class="form-control" min="1"
                                   value="${empty param.quantity ? 1 : param.quantity}"/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Actif</label>
                            <select name="active" class="form-select">
                                <option value="1" selected>Oui</option>
                                <option value="0">Non</option>
                            </select>
                        </div>

                        <div class="text-end">
                            <button class="btn btn-primary" type="submit">Assigner</button>
                            <a class="btn btn-secondary" href="${ctx}/subscription">Annuler</a>
                        </div>
                    </form>
                </c:otherwise>
            </c:choose>

        </div>
    </div>

    <c:if test="${sessionScope.role == 'ADMIN'
               or sessionScope.role == 'BARMAN'
               or sessionScope.role == 'SECRETARY'}">
        <div class="d-flex gap-2">
            <a class="btn btn-outline-secondary" href="${ctx}/subscription">Retour à la liste</a>
        </div>
    </c:if>
    <script>
        function debounce(fn,d){let t;return(...a)=>{clearTimeout(t);t=setTimeout(()=>fn(...a),d)}}
        function initAutocomplete({input,hidden,menu,url}) {
            let items=[], idx=-1;
            function render(){
                menu.innerHTML="";
                if(!items.length){menu.style.display="none";return;}
                items.forEach((it,i)=>{
                    const div=document.createElement("div");
                    div.className="autocomplete-item"+(i===idx?" active":"");
                    div.textContent=it.label;
                    div.onmousedown=(e)=>{e.preventDefault(); select(it);}
                    menu.appendChild(div);
                });
                menu.style.display="block";
            }
            function select(it){ input.value=it.label; hidden.value=it.id; menu.style.display="none"; idx=-1; }

            const run=debounce(async()=>{
                hidden.value=""; const q=input.value.trim();
                if(q.length<2){menu.style.display="none";return;}
                try{
                    const res=await fetch(url+encodeURIComponent(q));
                    const data=await res.json();
                    items=Array.isArray(data)?data:[]; idx=-1; render();
                }catch(e){ console.warn("autocomplete error", e); }
            },200);

            input.addEventListener("input",run);
            input.addEventListener("keydown",(e)=>{
                if(!items.length) return;
                if(e.key==="ArrowDown"){idx=Math.min(items.length-1,idx+1);render();e.preventDefault();}
                if(e.key==="ArrowUp"){idx=Math.max(0,idx-1);render();e.preventDefault();}
                if(e.key==="Enter"){if(idx>=0){select(items[idx]);e.preventDefault();}}
                if(e.key==="Escape"){menu.style.display="none"; idx=-1;}
            });
            document.addEventListener("click",(e)=>{ if(!menu.contains(e.target)&&e.target!==input) menu.style.display="none"; });
        }

        document.addEventListener("DOMContentLoaded", ()=>{
            const base = "${ctx}";
            initAutocomplete({
                input:document.getElementById("userSearch"),
                hidden:document.getElementById("userId"),
                menu:document.getElementById("userResults"),
                url: base + "/api/search/users?q="
            });
            initAutocomplete({
                input:document.getElementById("subSearch"),
                hidden:document.getElementById("subscriptionId"),
                menu:document.getElementById("subResults"),
                url: base + "/api/search/subscriptions?q="
            });
        });
    </script>

</section>
