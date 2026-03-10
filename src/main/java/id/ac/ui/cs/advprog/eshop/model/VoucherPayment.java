package id.ac.ui.cs.advprog.eshop.model;

import java.util.Map;

public class VoucherPayment extends Payment {
    private static final String VOUCHER_CODE_KEY = "voucherCode";
    private static final String VOUCHER_PREFIX = "ESHOP";
    private static final int VOUCHER_LENGTH = 16;
    private static final int REQUIRED_DIGIT_COUNT = 8;

    public VoucherPayment(String id, Order order, Map<String, String> paymentData) {
        super(id, order, "VOUCHER", paymentData);
    }

    @Override
    protected String determineStatus() {
        String voucherCode = getPaymentData().get(VOUCHER_CODE_KEY);
        return isValidVoucherCode(voucherCode) ? "SUCCESS" : "REJECTED";
    }

    private boolean isValidVoucherCode(String voucherCode) {
        return voucherCode != null
                && voucherCode.length() == VOUCHER_LENGTH
                && voucherCode.startsWith(VOUCHER_PREFIX)
                && countDigits(voucherCode) == REQUIRED_DIGIT_COUNT;
    }

    private int countDigits(String value) {
        int count = 0;
        for (char current : value.toCharArray()) {
            if (Character.isDigit(current)) {
                count += 1;
            }
        }
        return count;
    }
}
