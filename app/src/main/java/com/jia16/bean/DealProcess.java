package com.jia16.bean;

/**
 * 在途交易 --bean
 */
public class DealProcess {


    /**
     * id : 33658
     * message : null
     * amount : {"amount":5113.71,"currency":"CNY"}
     * status : PENDING
     * createdAt : 1483524611855
     * account : {"id":230101,"authorized":null,"descriptionType":"INVESTMENT","balance":{"amount":5113.71,"currency":"CNY"},"frozen":{"amount":5113.71,"currency":"CNY"},"externalId":null}
     * type : WITHDRAW
     * channel : PLATFORM
     * uri : https://10.156.241.44/api/users/97071/accounts/230101/deposit-withdraw-records
     */

    private int id;
    private Object message;
    /**
     * amount : 5113.71
     * currency : CNY
     */

    private AmountBean amount;
    private String status;
    private long createdAt;
    /**
     * id : 230101
     * authorized : null
     * descriptionType : INVESTMENT
     * balance : {"amount":5113.71,"currency":"CNY"}
     * frozen : {"amount":5113.71,"currency":"CNY"}
     * externalId : null
     */

    private AccountBean account;
    private String type;
    private String channel;
    private String uri;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
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

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public AccountBean getAccount() {
        return account;
    }

    public void setAccount(AccountBean account) {
        this.account = account;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public static class AmountBean {
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

    public static class AccountBean {
        private int id;
        private Object authorized;
        private String descriptionType;
        /**
         * amount : 5113.71
         * currency : CNY
         */

        private BalanceBean balance;
        /**
         * amount : 5113.71
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
    }

    @Override
    public String toString() {
        return "DealProcess{" +
                "id=" + id +
                ", message=" + message +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", account=" + account +
                ", type='" + type + '\'' +
                ", channel='" + channel + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }
}
