package com.jia16.bean;

import java.util.List;

/**
 * 资金流水的bean
 */
public class AllDeal {


    /**
     * amount : 12
     * currency : CNY
     */

    private AmountBean amount;
    /**
     * amount : 0
     * currency : CNY
     */

    private BalanceBean balance;
    /**
     * amount : {"amount":12,"currency":"CNY"}
     * balance : {"amount":0,"currency":"CNY"}
     * causeId : 32408
     * causeType : WITHDRAW
     * items : [{"type":"ONLINE_WITHDRAW","amount":{"amount":12,"currency":"CNY"},"remark":null}]
     * createdAt : 1481781712083
     * userId : null
     * type : WITHDRAW
     */

    private int causeId;
    private String causeType;
    private long createdAt;
    private Object userId;
    private String type;
    /**
     * type : ONLINE_WITHDRAW
     * amount : {"amount":12,"currency":"CNY"}
     * remark : null
     */

    private List<ItemsBean> items;

    public AmountBean getAmount() {
        return amount;
    }

    public void setAmount(AmountBean amount) {
        this.amount = amount;
    }

    public BalanceBean getBalance() {
        return balance;
    }

    public void setBalance(BalanceBean balance) {
        this.balance = balance;
    }

    public int getCauseId() {
        return causeId;
    }

    public void setCauseId(int causeId) {
        this.causeId = causeId;
    }

    public String getCauseType() {
        return causeType;
    }

    public void setCauseType(String causeType) {
        this.causeType = causeType;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class AmountBean {
        private String amount;
        private String currency;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }

    public static class BalanceBean {
        private String amount;
        private String currency;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }

    public static class ItemsBean {
        private String type;
        /**
         * amount : 12
         * currency : CNY
         */

        private AmountBean amount;
        private Object remark;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public AmountBean getAmount() {
            return amount;
        }

        public void setAmount(AmountBean amount) {
            this.amount = amount;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public static class AmountBean {
            private String amount;
            private String currency;

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }
        }
    }

    @Override
    public String toString() {
        return "AllDeal{" +
                "amount=" + amount +
                ", balance=" + balance +
                ", causeId=" + causeId +
                ", causeType='" + causeType + '\'' +
                ", createdAt=" + createdAt +
                ", userId=" + userId +
                ", type='" + type + '\'' +
                ", items=" + items +
                '}';
    }
}
