package com.jia16.bean;

/**
 * 我要投资的bean
 */
public class InvestConstant {


    /**
     * canUseVoucherTag : canUseVoucher
     * catalog : JIASHI_V3
     * amount : {"amount":200000,"currency":"CNY"}
     * title : 新手专享15号P
     * remainingAmount : {"amount":190000,"currency":"CNY"}
     * currentInvestmentAmount : {"amount":10000,"currency":"CNY"}
     * investmentPolicy : {"@type":"BeginnerInvestmentPolicyJson","minimumInvestmentAmount":{"amount":100,"currency":"CNY"},"stepInvestmentAmount":{"amount":1,"currency":"CNY"},"maximumInvestmentAmount":{"amount":10000,"currency":"CNY"}}
     * instalmentPolicy : {"@type":"OncePrincipalAndInterestInstalmentPolicyJson","annualRate":0.1,"interval":{"count":9,"intervalUnit":"DAY"},"numberOfInstalments":1,"type":"ONCE_PRINCIPAL_AND_INTEREST"}
     * user : {"id":22}
     * config : {"investProfitFeeRate":0,"investProfitFeeBeneficiaryParty":"p2p-investor-profit","prepaymentPenaltyDays":null,"overduePenaltyRate":0,"overdueAdvanceDays":null,"overdueAdvanceParty":"risk","payOffDaysLimit":null,"prepaySettledInstalmentsCount":null,"prepayPendingInstalmentsCount":null,"graceDays":5,"redDays":999,"passDays":3,"tagName":"新手专享\u2022抵扣本金","tagDescription":"本产品投资500元，可用代金券10元；投资10000元，可用代金券20元；新手标每人仅可投资1次，最高可投1万元","expiryDate":"","valueDays":0,"financialAssetsHeldTerminationDate":"2017-01-11","financialAssetsHeldInceptionDate":"2016-08-26"}
     * status : FUNDING
     * id : 12444
     * type : BEGINNER_SUBJECT
     */

    private String canUseVoucherTag;
    private String catalog;
    /**
     * amount : 200000
     * currency : CNY
     */

    private AmountBean amount;
    private String title;

    /**
     * amount : 190000
     * currency : CNY
     */

    private RemainingAmountBean remainingAmount;
    /**
     * amount : 10000
     * currency : CNY
     */

    private CurrentInvestmentAmountBean currentInvestmentAmount;
    /**
     * @type : BeginnerInvestmentPolicyJson
     * minimumInvestmentAmount : {"amount":100,"currency":"CNY"}
     * stepInvestmentAmount : {"amount":1,"currency":"CNY"}
     * maximumInvestmentAmount : {"amount":10000,"currency":"CNY"}
     */

    private InvestmentPolicyBean investmentPolicy;
    /**
     * @type : OncePrincipalAndInterestInstalmentPolicyJson
     * annualRate : 0.1
     * interval : {"count":9,"intervalUnit":"DAY"}
     * numberOfInstalments : 1
     * type : ONCE_PRINCIPAL_AND_INTEREST
     */

    private InstalmentPolicyBean instalmentPolicy;
    /**
     * id : 22
     */

    private UserBean user;
    /**
     * investProfitFeeRate : 0
     * investProfitFeeBeneficiaryParty : p2p-investor-profit
     * prepaymentPenaltyDays : null
     * overduePenaltyRate : 0
     * overdueAdvanceDays : null
     * overdueAdvanceParty : risk
     * payOffDaysLimit : null
     * prepaySettledInstalmentsCount : null
     * prepayPendingInstalmentsCount : null
     * graceDays : 5
     * redDays : 999
     * passDays : 3
     * tagName : 新手专享•抵扣本金
     * tagDescription : 本产品投资500元，可用代金券10元；投资10000元，可用代金券20元；新手标每人仅可投资1次，最高可投1万元
     * expiryDate :
     * valueDays : 0
     * financialAssetsHeldTerminationDate : 2017-01-11
     * financialAssetsHeldInceptionDate : 2016-08-26
     */

    private ConfigBean config;
    private String status;
    private int id;
    private String type;
    private boolean transferable;

    public boolean isTransferable() {
        return transferable;
    }

    public void setTransferable(boolean transferable) {
        this.transferable = transferable;
    }

    public String getCanUseVoucherTag() {
        return canUseVoucherTag;
    }

    public void setCanUseVoucherTag(String canUseVoucherTag) {
        this.canUseVoucherTag = canUseVoucherTag;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public AmountBean getAmount() {
        return amount;
    }

    public void setAmount(AmountBean amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RemainingAmountBean getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(RemainingAmountBean remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public CurrentInvestmentAmountBean getCurrentInvestmentAmount() {
        return currentInvestmentAmount;
    }

    public void setCurrentInvestmentAmount(CurrentInvestmentAmountBean currentInvestmentAmount) {
        this.currentInvestmentAmount = currentInvestmentAmount;
    }

    public InvestmentPolicyBean getInvestmentPolicy() {
        return investmentPolicy;
    }

    public void setInvestmentPolicy(InvestmentPolicyBean investmentPolicy) {
        this.investmentPolicy = investmentPolicy;
    }

    public InstalmentPolicyBean getInstalmentPolicy() {
        return instalmentPolicy;
    }

    public void setInstalmentPolicy(InstalmentPolicyBean instalmentPolicy) {
        this.instalmentPolicy = instalmentPolicy;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public ConfigBean getConfig() {
        return config;
    }

    public void setConfig(ConfigBean config) {
        this.config = config;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

        @Override
        public String toString() {
            return "AmountBean{" +
                    "amount=" + amount +
                    ", currency='" + currency + '\'' +
                    '}';
        }
    }

    public static class RemainingAmountBean {
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

        @Override
        public String toString() {
            return "RemainingAmountBean{" +
                    "amount=" + amount +
                    ", currency='" + currency + '\'' +
                    '}';
        }
    }

    public static class CurrentInvestmentAmountBean {
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

        @Override
        public String toString() {
            return "CurrentInvestmentAmountBean{" +
                    "amount=" + amount +
                    ", currency='" + currency + '\'' +
                    '}';
        }
    }

    public static class InvestmentPolicyBean {
        /**
         * amount : 100
         * currency : CNY
         */

        private MinimumInvestmentAmountBean minimumInvestmentAmount;
        /**
         * amount : 1
         * currency : CNY
         */

        private StepInvestmentAmountBean stepInvestmentAmount;
        /**
         * amount : 10000
         * currency : CNY
         */

        private MaximumInvestmentAmountBean maximumInvestmentAmount;

        public MinimumInvestmentAmountBean getMinimumInvestmentAmount() {
            return minimumInvestmentAmount;
        }

        public void setMinimumInvestmentAmount(MinimumInvestmentAmountBean minimumInvestmentAmount) {
            this.minimumInvestmentAmount = minimumInvestmentAmount;
        }

        public StepInvestmentAmountBean getStepInvestmentAmount() {
            return stepInvestmentAmount;
        }

        public void setStepInvestmentAmount(StepInvestmentAmountBean stepInvestmentAmount) {
            this.stepInvestmentAmount = stepInvestmentAmount;
        }

        public MaximumInvestmentAmountBean getMaximumInvestmentAmount() {
            return maximumInvestmentAmount;
        }

        public void setMaximumInvestmentAmount(MaximumInvestmentAmountBean maximumInvestmentAmount) {
            this.maximumInvestmentAmount = maximumInvestmentAmount;
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

        @Override
        public String toString() {
            return "InvestmentPolicyBean{" +
                    "minimumInvestmentAmount=" + minimumInvestmentAmount +
                    ", stepInvestmentAmount=" + stepInvestmentAmount +
                    ", maximumInvestmentAmount=" + maximumInvestmentAmount +
                    '}';
        }
    }

    public static class InstalmentPolicyBean {
        private double annualRate;
        /**
         * count : 9
         * intervalUnit : DAY
         */

        private IntervalBean interval;
        private int numberOfInstalments;
        private String type;

        public double getAnnualRate() {
            return annualRate;
        }

        public void setAnnualRate(double annualRate) {
            this.annualRate = annualRate;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

        @Override
        public String toString() {
            return "InstalmentPolicyBean{" +
                    "annualRate=" + annualRate +
                    ", interval=" + interval +
                    ", numberOfInstalments=" + numberOfInstalments +
                    ", type='" + type + '\'' +
                    '}';
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

        @Override
        public String toString() {
            return "UserBean{" +
                    "id=" + id +
                    '}';
        }
    }

    public static class ConfigBean {
        private int investProfitFeeRate;
        private String investProfitFeeBeneficiaryParty;
        private Object prepaymentPenaltyDays;
        private int overduePenaltyRate;
        private Object overdueAdvanceDays;
        private String overdueAdvanceParty;
        private Object payOffDaysLimit;
        private Object prepaySettledInstalmentsCount;
        private Object prepayPendingInstalmentsCount;
        private int graceDays;
        private int redDays;
        private int passDays;
        private String tagName;
        private String tagDescription;
        private String expiryDate;
        private int valueDays;
        private String financialAssetsHeldTerminationDate;
        private String financialAssetsHeldInceptionDate;

        public int getInvestProfitFeeRate() {
            return investProfitFeeRate;
        }

        public void setInvestProfitFeeRate(int investProfitFeeRate) {
            this.investProfitFeeRate = investProfitFeeRate;
        }

        public String getInvestProfitFeeBeneficiaryParty() {
            return investProfitFeeBeneficiaryParty;
        }

        public void setInvestProfitFeeBeneficiaryParty(String investProfitFeeBeneficiaryParty) {
            this.investProfitFeeBeneficiaryParty = investProfitFeeBeneficiaryParty;
        }

        public Object getPrepaymentPenaltyDays() {
            return prepaymentPenaltyDays;
        }

        public void setPrepaymentPenaltyDays(Object prepaymentPenaltyDays) {
            this.prepaymentPenaltyDays = prepaymentPenaltyDays;
        }

        public int getOverduePenaltyRate() {
            return overduePenaltyRate;
        }

        public void setOverduePenaltyRate(int overduePenaltyRate) {
            this.overduePenaltyRate = overduePenaltyRate;
        }

        public Object getOverdueAdvanceDays() {
            return overdueAdvanceDays;
        }

        public void setOverdueAdvanceDays(Object overdueAdvanceDays) {
            this.overdueAdvanceDays = overdueAdvanceDays;
        }

        public String getOverdueAdvanceParty() {
            return overdueAdvanceParty;
        }

        public void setOverdueAdvanceParty(String overdueAdvanceParty) {
            this.overdueAdvanceParty = overdueAdvanceParty;
        }

        public Object getPayOffDaysLimit() {
            return payOffDaysLimit;
        }

        public void setPayOffDaysLimit(Object payOffDaysLimit) {
            this.payOffDaysLimit = payOffDaysLimit;
        }

        public Object getPrepaySettledInstalmentsCount() {
            return prepaySettledInstalmentsCount;
        }

        public void setPrepaySettledInstalmentsCount(Object prepaySettledInstalmentsCount) {
            this.prepaySettledInstalmentsCount = prepaySettledInstalmentsCount;
        }

        public Object getPrepayPendingInstalmentsCount() {
            return prepayPendingInstalmentsCount;
        }

        public void setPrepayPendingInstalmentsCount(Object prepayPendingInstalmentsCount) {
            this.prepayPendingInstalmentsCount = prepayPendingInstalmentsCount;
        }

        public int getGraceDays() {
            return graceDays;
        }

        public void setGraceDays(int graceDays) {
            this.graceDays = graceDays;
        }

        public int getRedDays() {
            return redDays;
        }

        public void setRedDays(int redDays) {
            this.redDays = redDays;
        }

        public int getPassDays() {
            return passDays;
        }

        public void setPassDays(int passDays) {
            this.passDays = passDays;
        }

        public String getTagName() {
            return tagName;
        }

        public void setTagName(String tagName) {
            this.tagName = tagName;
        }

        public String getTagDescription() {
            return tagDescription;
        }

        public void setTagDescription(String tagDescription) {
            this.tagDescription = tagDescription;
        }

        public String getExpiryDate() {
            return expiryDate;
        }

        public void setExpiryDate(String expiryDate) {
            this.expiryDate = expiryDate;
        }

        public int getValueDays() {
            return valueDays;
        }

        public void setValueDays(int valueDays) {
            this.valueDays = valueDays;
        }

        public String getFinancialAssetsHeldTerminationDate() {
            return financialAssetsHeldTerminationDate;
        }

        public void setFinancialAssetsHeldTerminationDate(String financialAssetsHeldTerminationDate) {
            this.financialAssetsHeldTerminationDate = financialAssetsHeldTerminationDate;
        }

        public String getFinancialAssetsHeldInceptionDate() {
            return financialAssetsHeldInceptionDate;
        }

        public void setFinancialAssetsHeldInceptionDate(String financialAssetsHeldInceptionDate) {
            this.financialAssetsHeldInceptionDate = financialAssetsHeldInceptionDate;
        }

        @Override
        public String toString() {
            return "ConfigBean{" +
                    "investProfitFeeRate=" + investProfitFeeRate +
                    ", investProfitFeeBeneficiaryParty='" + investProfitFeeBeneficiaryParty + '\'' +
                    ", prepaymentPenaltyDays=" + prepaymentPenaltyDays +
                    ", overduePenaltyRate=" + overduePenaltyRate +
                    ", overdueAdvanceDays=" + overdueAdvanceDays +
                    ", overdueAdvanceParty='" + overdueAdvanceParty + '\'' +
                    ", payOffDaysLimit=" + payOffDaysLimit +
                    ", prepaySettledInstalmentsCount=" + prepaySettledInstalmentsCount +
                    ", prepayPendingInstalmentsCount=" + prepayPendingInstalmentsCount +
                    ", graceDays=" + graceDays +
                    ", redDays=" + redDays +
                    ", passDays=" + passDays +
                    ", tagName='" + tagName + '\'' +
                    ", tagDescription='" + tagDescription + '\'' +
                    ", expiryDate='" + expiryDate + '\'' +
                    ", valueDays=" + valueDays +
                    ", financialAssetsHeldTerminationDate='" + financialAssetsHeldTerminationDate + '\'' +
                    ", financialAssetsHeldInceptionDate='" + financialAssetsHeldInceptionDate + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "InvestConstant{" +
                "canUseVoucherTag='" + canUseVoucherTag + '\'' +
                ", catalog='" + catalog + '\'' +
                ", amount=" + amount +
                ", title='" + title + '\'' +
                ", remainingAmount=" + remainingAmount +
                ", currentInvestmentAmount=" + currentInvestmentAmount +
                ", investmentPolicy=" + investmentPolicy +
                ", instalmentPolicy=" + instalmentPolicy +
                ", user=" + user +
                ", config=" + config +
                ", status='" + status + '\'' +
                ", id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}
