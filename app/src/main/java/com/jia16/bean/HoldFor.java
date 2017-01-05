package com.jia16.bean;

/**
 * 持有中的javabean
 */
public class HoldFor {


    /**
     * id : 341268
     * amount : {"amount":5070,"currency":"CNY"}
     * accountReceivable : {"amount":6053.58,"currency":"CNY"}
     * contract : {"id":22827,"status":"PENDING","effectDate":1480734007041}
     * subject : {"amount":{"amount":488547,"currency":"CNY"},"id":11275,"title":"香帕二期01号A","catalog":"JIASHI_V2","investmentPolicy":{"stepInvestmentAmount":{"amount":1,"currency":"CNY"},"minimumInvestmentAmount":{"amount":5000,"currency":"CNY"}},"transferableStartDate":1481990400000,"subjectPid":0,"minimumTransferableAmount":{"amount":5000,"currency":"CNY"},"user":{"id":83438},"transferableEndDate":1542038400000,"instalmentPolicy":{"type":"ONCE_PRINCIPAL_AND_INTEREST","interval":{"count":720,"intervalUnit":"DAY"},"numberOfInstalments":1,"annualRate":0.09699999999999999}}
     * transferableAmount : {"amount":5070,"currency":"CNY"}
     * remainingDaysToBeTransferable : -3
     * holdingAmount : {"amount":5070,"currency":"CNY"}
     * user : {"id":97071}
     * transferableNow : true
     * transferable : true
     * nextSettlementDate : 1542902400000
     */

    private int id;
    /**
     * amount : 5070
     * currency : CNY
     */

    private AmountBean amount;
    /**
     * amount : 6053.58
     * currency : CNY
     */

    private AccountReceivableBean accountReceivable;
    /**
     * id : 22827
     * status : PENDING
     * effectDate : 1480734007041
     */

    private ContractBean contract;
    /**
     * amount : {"amount":488547,"currency":"CNY"}
     * id : 11275
     * title : 香帕二期01号A
     * catalog : JIASHI_V2
     * investmentPolicy : {"stepInvestmentAmount":{"amount":1,"currency":"CNY"},"minimumInvestmentAmount":{"amount":5000,"currency":"CNY"}}
     * transferableStartDate : 1481990400000
     * subjectPid : 0
     * minimumTransferableAmount : {"amount":5000,"currency":"CNY"}
     * user : {"id":83438}
     * transferableEndDate : 1542038400000
     * instalmentPolicy : {"type":"ONCE_PRINCIPAL_AND_INTEREST","interval":{"count":720,"intervalUnit":"DAY"},"numberOfInstalments":1,"annualRate":0.09699999999999999}
     */

    private SubjectBean subject;
    /**
     * amount : 5070
     * currency : CNY
     */

    private TransferableAmountBean transferableAmount;
    private int remainingDaysToBeTransferable;
    /**
     * amount : 5070
     * currency : CNY
     */

    private HoldingAmountBean holdingAmount;
    /**
     * id : 97071
     */

    private UserBean user;
    private boolean transferableNow;
    private boolean transferable;
    private long nextSettlementDate;

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

    public TransferableAmountBean getTransferableAmount() {
        return transferableAmount;
    }

    public void setTransferableAmount(TransferableAmountBean transferableAmount) {
        this.transferableAmount = transferableAmount;
    }

    public int getRemainingDaysToBeTransferable() {
        return remainingDaysToBeTransferable;
    }

    public void setRemainingDaysToBeTransferable(int remainingDaysToBeTransferable) {
        this.remainingDaysToBeTransferable = remainingDaysToBeTransferable;
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

    public boolean isTransferableNow() {
        return transferableNow;
    }

    public void setTransferableNow(boolean transferableNow) {
        this.transferableNow = transferableNow;
    }

    public boolean isTransferable() {
        return transferable;
    }

    public void setTransferable(boolean transferable) {
        this.transferable = transferable;
    }

    public long getNextSettlementDate() {
        return nextSettlementDate;
    }

    public void setNextSettlementDate(long nextSettlementDate) {
        this.nextSettlementDate = nextSettlementDate;
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
         * amount : 488547
         * currency : CNY
         */

        private AmountBean amount;
        private int id;
        private String title;
        private String catalog;
        /**
         * stepInvestmentAmount : {"amount":1,"currency":"CNY"}
         * minimumInvestmentAmount : {"amount":5000,"currency":"CNY"}
         */

        private InvestmentPolicyBean investmentPolicy;
        private long transferableStartDate;
        private int subjectPid;
        /**
         * amount : 5000
         * currency : CNY
         */

        private MinimumTransferableAmountBean minimumTransferableAmount;
        /**
         * id : 83438
         */

        private UserBean user;
        private long transferableEndDate;
        /**
         * type : ONCE_PRINCIPAL_AND_INTEREST
         * interval : {"count":720,"intervalUnit":"DAY"}
         * numberOfInstalments : 1
         * annualRate : 0.09699999999999999
         */

        private InstalmentPolicyBean instalmentPolicy;

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

        public String getCatalog() {
            return catalog;
        }

        public void setCatalog(String catalog) {
            this.catalog = catalog;
        }

        public InvestmentPolicyBean getInvestmentPolicy() {
            return investmentPolicy;
        }

        public void setInvestmentPolicy(InvestmentPolicyBean investmentPolicy) {
            this.investmentPolicy = investmentPolicy;
        }

        public long getTransferableStartDate() {
            return transferableStartDate;
        }

        public void setTransferableStartDate(long transferableStartDate) {
            this.transferableStartDate = transferableStartDate;
        }

        public int getSubjectPid() {
            return subjectPid;
        }

        public void setSubjectPid(int subjectPid) {
            this.subjectPid = subjectPid;
        }

        public MinimumTransferableAmountBean getMinimumTransferableAmount() {
            return minimumTransferableAmount;
        }

        public void setMinimumTransferableAmount(MinimumTransferableAmountBean minimumTransferableAmount) {
            this.minimumTransferableAmount = minimumTransferableAmount;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public long getTransferableEndDate() {
            return transferableEndDate;
        }

        public void setTransferableEndDate(long transferableEndDate) {
            this.transferableEndDate = transferableEndDate;
        }

        public InstalmentPolicyBean getInstalmentPolicy() {
            return instalmentPolicy;
        }

        public void setInstalmentPolicy(InstalmentPolicyBean instalmentPolicy) {
            this.instalmentPolicy = instalmentPolicy;
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

        public static class InvestmentPolicyBean {
            /**
             * amount : 1
             * currency : CNY
             */

            private StepInvestmentAmountBean stepInvestmentAmount;
            /**
             * amount : 5000
             * currency : CNY
             */

            private MinimumInvestmentAmountBean minimumInvestmentAmount;

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
        }

        public static class MinimumTransferableAmountBean {
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
             * count : 720
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
                private int count;
                private String intervalUnit;

                public int getCount() {
                    return count;
                }

                public void setCount(int count) {
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
    }

    public static class TransferableAmountBean {
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
        return "HoldFor{" +
                "id=" + id +
                ", amount=" + amount +
                ", accountReceivable=" + accountReceivable +
                ", contract=" + contract +
                ", subject=" + subject +
                ", transferableAmount=" + transferableAmount +
                ", remainingDaysToBeTransferable=" + remainingDaysToBeTransferable +
                ", holdingAmount=" + holdingAmount +
                ", user=" + user +
                ", transferableNow=" + transferableNow +
                ", transferable=" + transferable +
                ", nextSettlementDate=" + nextSettlementDate +
                '}';
    }
}
