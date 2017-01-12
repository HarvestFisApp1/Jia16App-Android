package com.jia16.bean;

/**
 * 我要投资--转让变现--bean
 */
public class TransferMoney {


    /**
     * amount : 5084.46
     * currency : CNY
     */

    private RemainingAmountBean remainingAmount;
    /**
     * remainingAmount : {"amount":5084.46,"currency":"CNY"}
     * status : FUNDING
     * catalog : JIASHI_V5
     * type : TRANSFER
     * remainingTransferDays : 4
     * canUseVoucherTag : noUseVoucher
     * currentInvestmentAmount : {"amount":0,"currency":"CNY"}
     * id : 12605
     * amount : {"amount":5000,"currency":"CNY"}
     * title : 香帕二期01号A 17010040
     * transferableStartDays : 15
     * transferFee : {"amount":0,"currency":"CNY"}
     * transferAmount : {"amount":5084.46,"currency":"CNY"}
     * investmentPolicy : {"@type":"TransferInvestmentPolicyJson","investmentAmount":{"amount":5084.46,"currency":"CNY"}}
     * config : {"prepaySettledInstalmentsCount":null,"overdueAdvanceParty":"risk","financialAssetsHeldInceptionDate":"2016-11-25","passDays":5,"graceDays":7,"valueDays":0,"overduePenaltyRate":0,"transferFeeRate":0,"payOffDaysLimit":null,"overdueAdvanceDays":null,"prepaymentPenaltyDays":null,"prepayPendingInstalmentsCount":null,"transferableStartDays":15,"transferableEndDays":10,"investProfitFeeRate":0,"investProfitFeeBeneficiaryParty":"p2p-investor-profit","financialAssetsHeldTerminationDate":"2018-11-23","redDays":999}
     * user : {"id":2179}
     * transferable : true
     * instalmentPolicy : {"type":"ONCE_PRINCIPAL_AND_INTEREST","interval":{"count":687,"intervalUnit":"DAY"},"numberOfInstalments":1,"annualRate":0.09126602,"@type":"OncePrincipalAndInterestInstalmentPolicyJson"}
     */

    private String status;
    private String catalog;
    private String type;
    private int remainingTransferDays;
    private String canUseVoucherTag;
    /**
     * amount : 0
     * currency : CNY
     */

    private CurrentInvestmentAmountBean currentInvestmentAmount;
    private int id;
    /**
     * amount : 5000
     * currency : CNY
     */

    private AmountBean amount;
    private String title;
    private int transferableStartDays;
    /**
     * amount : 0
     * currency : CNY
     */

    private TransferFeeBean transferFee;
    /**
     * amount : 5084.46
     * currency : CNY
     */

    private TransferAmountBean transferAmount;
    /**
     * @type : TransferInvestmentPolicyJson
     * investmentAmount : {"amount":5084.46,"currency":"CNY"}
     */

    private InvestmentPolicyBean investmentPolicy;
    /**
     * prepaySettledInstalmentsCount : null
     * overdueAdvanceParty : risk
     * financialAssetsHeldInceptionDate : 2016-11-25
     * passDays : 5
     * graceDays : 7
     * valueDays : 0
     * overduePenaltyRate : 0
     * transferFeeRate : 0
     * payOffDaysLimit : null
     * overdueAdvanceDays : null
     * prepaymentPenaltyDays : null
     * prepayPendingInstalmentsCount : null
     * transferableStartDays : 15
     * transferableEndDays : 10
     * investProfitFeeRate : 0
     * investProfitFeeBeneficiaryParty : p2p-investor-profit
     * financialAssetsHeldTerminationDate : 2018-11-23
     * redDays : 999
     */

    private ConfigBean config;
    /**
     * id : 2179
     */

    private UserBean user;
    private boolean transferable;
    /**
     * type : ONCE_PRINCIPAL_AND_INTEREST
     * interval : {"count":687,"intervalUnit":"DAY"}
     * numberOfInstalments : 1
     * annualRate : 0.09126602
     * @type : OncePrincipalAndInterestInstalmentPolicyJson
     */

    private InstalmentPolicyBean instalmentPolicy;

    public RemainingAmountBean getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(RemainingAmountBean remainingAmount) {
        this.remainingAmount = remainingAmount;
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

    public int getRemainingTransferDays() {
        return remainingTransferDays;
    }

    public void setRemainingTransferDays(int remainingTransferDays) {
        this.remainingTransferDays = remainingTransferDays;
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

    public TransferFeeBean getTransferFee() {
        return transferFee;
    }

    public void setTransferFee(TransferFeeBean transferFee) {
        this.transferFee = transferFee;
    }

    public TransferAmountBean getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(TransferAmountBean transferAmount) {
        this.transferAmount = transferAmount;
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

    public static class TransferFeeBean {
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

    public static class TransferAmountBean {
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
         * amount : 5084.46
         * currency : CNY
         */

        private InvestmentAmountBean investmentAmount;

        public InvestmentAmountBean getInvestmentAmount() {
            return investmentAmount;
        }

        public void setInvestmentAmount(InvestmentAmountBean investmentAmount) {
            this.investmentAmount = investmentAmount;
        }

        public static class InvestmentAmountBean {
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
        private String overdueAdvanceParty;
        private String financialAssetsHeldInceptionDate;
        private int passDays;
        private int graceDays;
        private int valueDays;
        private double overduePenaltyRate;
        private double transferFeeRate;
        private Object payOffDaysLimit;
        private Object overdueAdvanceDays;
        private Object prepaymentPenaltyDays;
        private Object prepayPendingInstalmentsCount;
        private int transferableStartDays;
        private int transferableEndDays;
        private double investProfitFeeRate;
        private String investProfitFeeBeneficiaryParty;
        private String financialAssetsHeldTerminationDate;
        private int redDays;

        public Object getPrepaySettledInstalmentsCount() {
            return prepaySettledInstalmentsCount;
        }

        public void setPrepaySettledInstalmentsCount(Object prepaySettledInstalmentsCount) {
            this.prepaySettledInstalmentsCount = prepaySettledInstalmentsCount;
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

        public double getOverduePenaltyRate() {
            return overduePenaltyRate;
        }

        public void setOverduePenaltyRate(double overduePenaltyRate) {
            this.overduePenaltyRate = overduePenaltyRate;
        }

        public double getTransferFeeRate() {
            return transferFeeRate;
        }

        public void setTransferFeeRate(double transferFeeRate) {
            this.transferFeeRate = transferFeeRate;
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

        public double getInvestProfitFeeRate() {
            return investProfitFeeRate;
        }

        public void setInvestProfitFeeRate(double investProfitFeeRate) {
            this.investProfitFeeRate = investProfitFeeRate;
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
         * count : 687
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
        return "TransferMoney{" +
                "remainingAmount=" + remainingAmount +
                ", status='" + status + '\'' +
                ", catalog='" + catalog + '\'' +
                ", type='" + type + '\'' +
                ", remainingTransferDays=" + remainingTransferDays +
                ", canUseVoucherTag='" + canUseVoucherTag + '\'' +
                ", currentInvestmentAmount=" + currentInvestmentAmount +
                ", id=" + id +
                ", amount=" + amount +
                ", title='" + title + '\'' +
                ", transferableStartDays=" + transferableStartDays +
                ", transferFee=" + transferFee +
                ", transferAmount=" + transferAmount +
                ", investmentPolicy=" + investmentPolicy +
                ", config=" + config +
                ", user=" + user +
                ", transferable=" + transferable +
                ", instalmentPolicy=" + instalmentPolicy +
                '}';
    }
}
