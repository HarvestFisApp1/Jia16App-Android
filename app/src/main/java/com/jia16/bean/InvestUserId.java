package com.jia16.bean;

/**
 * 我要投资--投资详情--获取可用投资金额前获取用户id的bean
 */
public class InvestUserId {


    /**
     * id : 230100
     * authorized : null
     * descriptionType : LOAN
     * balance : {"amount":0,"currency":"CNY"}
     * frozen : {"amount":0,"currency":"CNY"}
     * externalId : null
     */

    private int id;
    private Object authorized;
    private String descriptionType;
    /**
     * amount : 0
     * currency : CNY
     */

    private BalanceBean balance;
    /**
     * amount : 0
     * currency : CNY
     */

    private FrozenBean frozen;
    private Object externalId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getAuthorized() {
        return authorized;
    }

    public void setAuthorized(Object authorized) {
        this.authorized = authorized;
    }

    public String getDescriptionType() {
        return descriptionType;
    }

    public void setDescriptionType(String descriptionType) {
        this.descriptionType = descriptionType;
    }

    public BalanceBean getBalance() {
        return balance;
    }

    public void setBalance(BalanceBean balance) {
        this.balance = balance;
    }

    public FrozenBean getFrozen() {
        return frozen;
    }

    public void setFrozen(FrozenBean frozen) {
        this.frozen = frozen;
    }

    public Object getExternalId() {
        return externalId;
    }

    public void setExternalId(Object externalId) {
        this.externalId = externalId;
    }

    public static class BalanceBean {
        private double amount;
        private String currency;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }

    public static class FrozenBean {
        private double amount;
        private String currency;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }

    @Override
    public String toString() {
        return "InvestUserId{" +
                "id=" + id +
                ", authorized=" + authorized +
                ", descriptionType='" + descriptionType + '\'' +
                ", balance=" + balance +
                ", frozen=" + frozen +
                ", externalId=" + externalId +
                '}';
    }
}
