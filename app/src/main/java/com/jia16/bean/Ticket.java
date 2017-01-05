package com.jia16.bean;

/**
 * 代金券java bean
 */
public class Ticket {

    /**
     * id : 1073590
     * amount : {"amount":10,"currency":"CNY"}
     * status : UNUSED
     * expireDate : 2016-12-16
     */

    private int id;
    /**
     * amount : 10
     * currency : CNY
     */

    private AmountBean amount;
    private String status;
    private String expireDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AmountBean getAmount() {
        return amount;
    }

    public void setAmount(AmountBean amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public static class AmountBean {
        private int amount;
        private String currency;

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        @Override
        public String toString() {
            return "AmountBean{" +
                    "amount=" + amount +
                    ", currency='" + currency + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", expireDate='" + expireDate + '\'' +
                '}';
    }
}
