package com.jia16.bean;

/**
 * 首页条目--固定收益bean
 */
public class FixedEarn {

    /**
     * guaranteeable : false
     * remainingAmount : {"amount":274433,"currency":"CNY"}
     * mortgageable : false
     * status : FUNDING
     * catalog : JIASHI_V2
     * type : P2P_LOAN
     * canUseVoucherTag : canUseVoucher
     * currentInvestmentAmount : {"amount":225567,"currency":"CNY"}
     * id : 12649
     * amount : {"amount":500000,"currency":"CNY"}
     * title : 信达05号Q
     * transferableStartDays : 15
     * investmentPolicy : {"stepInvestmentAmount":{"amount":1,"currency":"CNY"},"minimumInvestmentAmount":{"amount":10000,"currency":"CNY"},"@type":"DefaultInvestmentPolicyJson"}
     * config : {"prepaySettledInstalmentsCount":null,"expiryDate":"","overdueAdvanceParty":"risk","financialAssetsHeldInceptionDate":"2016-10-24","passDays":1,"graceDays":3,"valueDays":0,"overduePenaltyRate":0,"payOffDaysLimit":null,"overdueAdvanceDays":null,"prepaymentPenaltyDays":null,"prepayPendingInstalmentsCount":null,"transferableStartDays":15,"transferableEndDays":10,"investProfitFeeRate":0,"tagDescription":"该产品每投资1万可抵10元、投资5万可抵60元、投资10万可抵150元；产品次日起息成立；持有满15天可转让","tagName":"优质民企·可用代金券","investProfitFeeBeneficiaryParty":"p2p-investor-profit","financialAssetsHeldTerminationDate":"2017-04-20","redDays":999}
     * user : {"id":96391}
     * transferable : true
     * instalmentPolicy : {"type":"ONCE_PRINCIPAL_AND_INTEREST","interval":{"count":104,"intervalUnit":"DAY"},"numberOfInstalments":1,"annualRate":0.0795,"@type":"OncePrincipalAndInterestInstalmentPolicyJson"}
     */

    private boolean guaranteeable;
    /**
     * amount : 274433
     * currency : CNY
     */

    private RemainingAmountBean remainingAmount;
    private boolean mortgageable;
    private String status;
    private String catalog;
    private String type;
    private String canUseVoucherTag;
    /**
     * amount : 225567
     * currency : CNY
     */

    private CurrentInvestmentAmountBean currentInvestmentAmount;
    private int id;
    /**
     * amount : 500000
     * currency : CNY
     */

    private AmountBean amount;
    private String title;
    private int transferableStartDays;
    /**
     * stepInvestmentAmount : {"amount":1,"currency":"CNY"}
     * minimumInvestmentAmount : {"amount":10000,"currency":"CNY"}
     * @type : DefaultInvestmentPolicyJson
     */

    private InvestmentPolicyBean investmentPolicy;
    /**
     * prepaySettledInstalmentsCount : null
     * expiryDate :
     * overdueAdvanceParty : risk
     * financialAssetsHeldInceptionDate : 2016-10-24
     * passDays : 1
     * graceDays : 3
     * valueDays : 0
     * overduePenaltyRate : 0
     * payOffDaysLimit : null
     * overdueAdvanceDays : null
     * prepaymentPenaltyDays : null
     * prepayPendingInstalmentsCount : null
     * transferableStartDays : 15
     * transferableEndDays : 10
     * investProfitFeeRate : 0
     * tagDescription : 该产品每投资1万可抵10元、投资5万可抵60元、投资10万可抵150元；产品次日起息成立；持有满15天可转让
     * tagName : 优质民企·可用代金券
     * investProfitFeeBeneficiaryParty : p2p-investor-profit
     * financialAssetsHeldTerminationDate : 2017-04-20
     * redDays : 999
     */

    private ConfigBean config;
    /**
     * id : 96391
     */

    private UserBean user;
    private boolean transferable;
    /**
     * type : ONCE_PRINCIPAL_AND_INTEREST
     * interval : {"count":104,"intervalUnit":"DAY"}
     * numberOfInstalments : 1
     * annualRate : 0.0795
     * @type : OncePrincipalAndInterestInstalmentPolicyJson
     */

    private InstalmentPolicyBean instalmentPolicy;

    public boolean isGuaranteeable() {
        return guaranteeable;
    }

    public void setGuaranteeable(boolean guaranteeable) {
        this.guaranteeable = guaranteeable;
    }

    public RemainingAmountBean getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(RemainingAmountBean remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public boolean isMortgageable() {
        return mortgageable;
    }

    public void setMortgageable(boolean mortgageable) {
        this.mortgageable = mortgageable;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCanUseVoucherTag() {
        return canUseVoucherTag;
    }

    public void setCanUseVoucherTag(String canUseVoucherTag) {
        this.canUseVoucherTag = canUseVoucherTag;
    }

    public CurrentInvestmentAmountBean getCurrentInvestmentAmount() {
        return currentInvestmentAmount;
    }

    public void setCurrentInvestmentAmount(CurrentInvestmentAmountBean currentInvestmentAmount) {
        this.currentInvestmentAmount = currentInvestmentAmount;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTransferableStartDays() {
        return transferableStartDays;
    }

    public void setTransferableStartDays(int transferableStartDays) {
        this.transferableStartDays = transferableStartDays;
    }

    public InvestmentPolicyBean getInvestmentPolicy() {
        return investmentPolicy;
    }

    public void setInvestmentPolicy(InvestmentPolicyBean investmentPolicy) {
        this.investmentPolicy = investmentPolicy;
    }

    public ConfigBean getConfig() {
        return config;
    }

    public void setConfig(ConfigBean config) {
        this.config = config;
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

    public InstalmentPolicyBean getInstalmentPolicy() {
        return instalmentPolicy;
    }

    public void setInstalmentPolicy(InstalmentPolicyBean instalmentPolicy) {
        this.instalmentPolicy = instalmentPolicy;
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
         * amount : 10000
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

    public static class ConfigBean {
        private Object prepaySettledInstalmentsCount;
        private String expiryDate;
        private String overdueAdvanceParty;
        private String financialAssetsHeldInceptionDate;
        private int passDays;
        private int graceDays;
        private int valueDays;
        private int overduePenaltyRate;
        private Object payOffDaysLimit;
        private Object overdueAdvanceDays;
        private Object prepaymentPenaltyDays;
        private Object prepayPendingInstalmentsCount;
        private int transferableStartDays;
        private int transferableEndDays;
        private int investProfitFeeRate;
        private String tagDescription;
        private String tagName;
        private String investProfitFeeBeneficiaryParty;
        private String financialAssetsHeldTerminationDate;
        private int redDays;

        public Object getPrepaySettledInstalmentsCount() {
            return prepaySettledInstalmentsCount;
        }

        public void setPrepaySettledInstalmentsCount(Object prepaySettledInstalmentsCount) {
            this.prepaySettledInstalmentsCount = prepaySettledInstalmentsCount;
        }

        public String getExpiryDate() {
            return expiryDate;
        }

        public void setExpiryDate(String expiryDate) {
            this.expiryDate = expiryDate;
        }

        public String getOverdueAdvanceParty() {
            return overdueAdvanceParty;
        }

        public void setOverdueAdvanceParty(String overdueAdvanceParty) {
            this.overdueAdvanceParty = overdueAdvanceParty;
        }

        public String getFinancialAssetsHeldInceptionDate() {
            return financialAssetsHeldInceptionDate;
        }

        public void setFinancialAssetsHeldInceptionDate(String financialAssetsHeldInceptionDate) {
            this.financialAssetsHeldInceptionDate = financialAssetsHeldInceptionDate;
        }

        public int getPassDays() {
            return passDays;
        }

        public void setPassDays(int passDays) {
            this.passDays = passDays;
        }

        public int getGraceDays() {
            return graceDays;
        }

        public void setGraceDays(int graceDays) {
            this.graceDays = graceDays;
        }

        public int getValueDays() {
            return valueDays;
        }

        public void setValueDays(int valueDays) {
            this.valueDays = valueDays;
        }

        public int getOverduePenaltyRate() {
            return overduePenaltyRate;
        }

        public void setOverduePenaltyRate(int overduePenaltyRate) {
            this.overduePenaltyRate = overduePenaltyRate;
        }

        public Object getPayOffDaysLimit() {
            return payOffDaysLimit;
        }

        public void setPayOffDaysLimit(Object payOffDaysLimit) {
            this.payOffDaysLimit = payOffDaysLimit;
        }

        public Object getOverdueAdvanceDays() {
            return overdueAdvanceDays;
        }

        public void setOverdueAdvanceDays(Object overdueAdvanceDays) {
            this.overdueAdvanceDays = overdueAdvanceDays;
        }

        public Object getPrepaymentPenaltyDays() {
            return prepaymentPenaltyDays;
        }

        public void setPrepaymentPenaltyDays(Object prepaymentPenaltyDays) {
            this.prepaymentPenaltyDays = prepaymentPenaltyDays;
        }

        public Object getPrepayPendingInstalmentsCount() {
            return prepayPendingInstalmentsCount;
        }

        public void setPrepayPendingInstalmentsCount(Object prepayPendingInstalmentsCount) {
            this.prepayPendingInstalmentsCount = prepayPendingInstalmentsCount;
        }

        public int getTransferableStartDays() {
            return transferableStartDays;
        }

        public void setTransferableStartDays(int transferableStartDays) {
            this.transferableStartDays = transferableStartDays;
        }

        public int getTransferableEndDays() {
            return transferableEndDays;
        }

        public void setTransferableEndDays(int transferableEndDays) {
            this.transferableEndDays = transferableEndDays;
        }

        public int getInvestProfitFeeRate() {
            return investProfitFeeRate;
        }

        public void setInvestProfitFeeRate(int investProfitFeeRate) {
            this.investProfitFeeRate = investProfitFeeRate;
        }

        public String getTagDescription() {
            return tagDescription;
        }

        public void setTagDescription(String tagDescription) {
            this.tagDescription = tagDescription;
        }

        public String getTagName() {
            return tagName;
        }

        public void setTagName(String tagName) {
            this.tagName = tagName;
        }

        public String getInvestProfitFeeBeneficiaryParty() {
            return investProfitFeeBeneficiaryParty;
        }

        public void setInvestProfitFeeBeneficiaryParty(String investProfitFeeBeneficiaryParty) {
            this.investProfitFeeBeneficiaryParty = investProfitFeeBeneficiaryParty;
        }

        public String getFinancialAssetsHeldTerminationDate() {
            return financialAssetsHeldTerminationDate;
        }

        public void setFinancialAssetsHeldTerminationDate(String financialAssetsHeldTerminationDate) {
            this.financialAssetsHeldTerminationDate = financialAssetsHeldTerminationDate;
        }

        public int getRedDays() {
            return redDays;
        }

        public void setRedDays(int redDays) {
            this.redDays = redDays;
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
         * count : 104
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

    @Override
    public String toString() {
        return "FixedEarn{" +
                "guaranteeable=" + guaranteeable +
                ", remainingAmount=" + remainingAmount +
                ", mortgageable=" + mortgageable +
                ", status='" + status + '\'' +
                ", catalog='" + catalog + '\'' +
                ", type='" + type + '\'' +
                ", canUseVoucherTag='" + canUseVoucherTag + '\'' +
                ", currentInvestmentAmount=" + currentInvestmentAmount +
                ", id=" + id +
                ", amount=" + amount +
                ", title='" + title + '\'' +
                ", transferableStartDays=" + transferableStartDays +
                ", investmentPolicy=" + investmentPolicy +
                ", config=" + config +
                ", user=" + user +
                ", transferable=" + transferable +
                ", instalmentPolicy=" + instalmentPolicy +
                '}';
    }
}
