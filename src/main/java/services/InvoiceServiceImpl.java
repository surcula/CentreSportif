package services;

import Tools.Result;
import interfaces.InvoiceService;
import org.apache.log4j.Logger;

public class InvoiceServiceImpl implements InvoiceService {
    private static final Logger log = Logger.getLogger(InvoiceServiceImpl.class);

    @Override
    public Result<Boolean> generateForOrder(int orderId) {
        // Stub : journalise; plus tard, entité Invoice + PDF.
        log.info("Invoice generated for orderId=" + orderId);
        return Result.ok(Boolean.TRUE);
    }
}
