package com.jia16.bean;

/**
 * 已经回款的javabean
 */
public class Returnmoney {


    /**
     * id : 52175
     * amount : {"amount":2000,"currency":"CNY"}
     * accountReceivable : {"amount":2003.89,"currency":"CNY"}
     * contract : {"id":4729,"status":"DONE","effectDate":1476241216395}
     * subject : {"amount":{"amount":200000,"currency":"CNY"},"id":9110,"title":"新手专享13号I","user":{"id":22},"catalog":"JIASHI_V3","instalmentPolicy":{"type":"ONCE_PRINCIPAL_AND_INTEREST","interval":{"count":7,"intervalUnit":"DAY"},"numberOfInstalments":1,"annualRate":0.1},"investmentPolicy":{"stepInvestmentAmount":{"amount":1,"currency":"CNY"},"minimumInvestmentAmount":{"amount":100,"currency":"CNY"},"maximumInvestmentAmount":{"amount":10000,"currency":"CNY"}}}
     * holdingAmount : {"amount":2000,"currency":"CNY"}
     * user : {"id":97071}
     * transferable : false
     * transferableNow : false
     */

    private int id;
    /**
     * amount : 2000
     * currency : CNY
     */

    private AmountBean amount;
    /**
     * amount : 2003.89
     * currency : CNY
     */

    private AccountReceivableBean accountReceivable;
    /**
     * id : 4729
     * status : DONE
     * effectDate : 1476241216395
     */

    private ContractBean contract;
    /**
     * amount : {"amount":200000,"currency":"CNY"}
     * id : 9110
     * title : 新手专享13号I
     * user : {"id":22}
     * catalog : JIASHI_V3
     * instalmentPolicy : {"type":"ONCE_PRINCIPAL_AND_INTEREST","interval":{"count":7,"intervalUnit":"DAY"},"numberOfInstalments":1,"annualRate":0.1}
     * investmentPolicy : {"stepInvestmentAmount":{"amount":1,"currency":"CNY"},"minimumInvestmentAmount":{"amount":100,"currency":"CNY"},"maximumInvestmentAmount":{"amount":10000,"currency":"CNY"}}
     */

    private SubjectBean subject;
    /**
     * amount : 2000
     * currency : CNY
     */

    private HoldingAmountBean holdingAmount;
    /**
     * id : 97071
     */

    private UserBean user;
    private boolean transferable;
    private boolean transferableNow;

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

    public AccountReceivableBean getAccountReceivable() {
        return accountReceivable;
    }

    public void setAccountReceivable(AccountReceivableBean accountReceivable) {
        this.accountReceivable = accountReceivable;
    }

    public ContractBean getContract() {
        return contract;
    }

    public void setContract(ContractBean contract) {
        this.contract = contract;
    }

    public SubjectBean getSubject() {
        return subject;
    }

    public void setSubject(SubjectBean subject) {
        this.subject = subject;
    }

    public HoldingAmountBean getHoldingAmount() {
        return holdingAmount;
    }

    public void setHoldingAmount(HoldingAmountBean holdingAmount) {
        this.holdingAmount = holdingAmount;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public boolean isTransferable() {
        return transferable;
    }

    public void setTransferable(boolean transferable) {
        this.transferable = transferable;
    }

    public boolean isTransferableNow() {
        return transferableNow;
    }

    public void setTransferableNow(boolean transferableNow) {
        this.transferableNow = transferableNow;
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

    public static class AccountReceivableBean {
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

    public static class ContractBean {
        private int id;
        private String status;
        private long effectDate;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public long getEffectDate() {
            return effectDate;
        }

        public void setEffectDate(long effectDate) {
            this.effectDate = effectDate;
        }
    }

    public static class SubjectBean {
        /**
         * amount : 200000
         * currency : CNY
         */

        private AmountBean amount;
        private int id;
        private String title;
        /**
         * id : 22
         */

        private UserBean user;
        private String catalog;
        /**
         * type : ONCE_PRINCIPAL_AND_INTEREST
         * interval : {"count":7,"intervalUnit":"DAY"}
         * numberOfInstalments : 1
         * annualRate : 0.1
         */

        private InstalmentPolicyBean instalmentPolicy;
        /**
         * stepInvestmentAmount : {"amount":1,"currency":"CNY"}
         * minimumInvestmentAmount : {"amount":100,"currency":"CNY"}
         * maximumInvestmentAmount : {"amount":10000,"currency":"CNY"}
         */

        private InvestmentPolicyBean investmentPolicy;

        public AmountBean getAmount() {
            return amount;
        }

        public void setAmount(AmountBean amount) {
            this.amount = amount;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public String getCatalog() {
            return catalog;
        }

        public void setCatalog(String catalog) {
            this.catalog = catalog;
        }

        public InstalmentPolicyBean getInstalmentPolicy() {
            return instalmentPolicy;
        }

        public void setInstalmentPolicy(InstalmentPolicyBean instalmentPolicy) {
            this.instalmentPolicy = instalmentPolicy;
        }

        public InvestmentPolicyBean getInvestmentPolicy() {
            return investmentPolicy;
        }

        public void setInvestmentPolicy(InvestmentPolicyBean investmentPolicy) {
            this.investmentPolicy = investmentPolicy;
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

        public static class UserBean {
            private int id;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }

        public static class InstalmentPolicyBean {
            private String type;
            /**
             * count : 7
             * intervalUnit : DAY
             */

            private IntervalBean interval;
            private int numberOfInstalments;
            private double annualRate;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public IntervalBean getInterval() {
                return interval;
            }

            public void setInterval(IntervalBean interval) {
                this.interval = interval;
            }

            public int getNumberOfInstalments() {
                return numberOfInstalments;
            }

            public void setNumberOfInstalments(int numberOfInstalments) {
                this.numberOfInstalments = numberOfInstalments;
            }

            public double getAnnualRate() {
                return annualRate;
            }

            public void setAnnualRate(double annualRate) {
                this.annualRate = annualRate;
            }

            public static class IntervalBean {
                private double count;
                private String intervalUnit;

                public double getCount() {
                    return count;
                }

                public void setCount(double count) {
                    this.count = count;
                }

                public String getIntervalUnit() {
                    return intervalUnit;
                }

                public void setIntervalUnit(String intervalUnit) {
                    this.intervalUnit = intervalUnit;
                }
            }
        }

        public static class InvestmentPolicyBean {
            /**
             * amount : 1
             * currency : CNY
             */

            private StepInvestmentAmountBean stepInvestmentAmount;
            /**
             * amount : 100
             * currency : CNY
             */

            private MinimumInvestmentAmountBean minimumInvestmentAmount;
            /**
             * amount : 10000
             * currency : CNY
             */

            private MaximumInvestmentAmountBean maximumInvestmentAmount;

            public StepInvestmentAmountBean getStepInvestmentAmount() {
                return stepInvestmentAmount;
            }

            public void setStepInvestmentAmount(StepInvestmentAmountBean stepInvestmentAmount) {
                this.stepInvestmentAmount = stepInvestmentAmount;
            }

            public MinimumInvestmentAmountBean getMinimumInvestmentAmount() {
                return minimumInvestmentAmount;
            }

            public void setMinimumInvestmentAmount(MinimumInvestmentAmountBean minimumInvestmentAmount) {
                this.minimumInvestmentAmount = minimumInvestmentAmount;
            }

            public MaximumInvestmentAmountBean getMaximumInvestmentAmount() {
                return maximumInvestmentAmount;
            }

            public void setMaximumInvestmentAmount(MaximumInvestmentAmountBean maximumInvestmentAmount) {
                this.maximumInvestmentAmount = maximumInvestmentAmount;
            }

            public static class StepInvestmentAmountBean {
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

            public static class MinimumInvestmentAmountBean {
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

            public static class MaximumInvestmentAmountBean {
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
    }

    public static class HoldingAmountBean {
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

    public static class UserBean {
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    @Override
    public String toString() {
        return "Returnmoney{" +
                "id=" + id +
                ", amount=" + amount +
                ", accountReceivable=" + accountReceivable +
                ", contract=" + contract +
                ", subject=" + subject +
                ", holdingAmount=" + holdingAmount +
                ", user=" + user +
                ", transferable=" + transferable +
                ", transferableNow=" + transferableNow +
                '}';
    }
}
