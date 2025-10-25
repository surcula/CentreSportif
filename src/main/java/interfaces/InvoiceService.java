package interfaces;

import Tools.Result;

public interface InvoiceService {

    /**
     * Génère une facture pour une commande payée.
     * Retourne true si l’écriture facture (ou la génération PDF) s’est bien passée.
     */
    Result<Boolean> generateForOrder(int orderId);
}
