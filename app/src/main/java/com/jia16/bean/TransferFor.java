package com.jia16.bean;

/**
 * 转让中的bean
 */
public class TransferFor {


    /**
     * guaranteeable : false
     * remainingAmount : {"amount":5085.83,"currency":"CNY"}
     * mortgageable : false
     * status : FUNDING
     * contract : null
     * catalog : JIASHI_V5
     * type : TRANSFER
     * remainingTransferDays : 1
     * canUseVoucherTag : noUseVoucher
     * currentInvestmentAmount : {"amount":0,"currency":"CNY"}
     * id : 12972
     * amount : {"amount":5000,"currency":"CNY"}
     * title : 信管01号N 17010255
     * transferableStartDays : 15
     * transferAmount : {"amount":5085.83,"currency":"CNY"}
     * transferFee : {"amount":0,"currency":"CNY"}
     * investmentPolicy : {"investmentAmount":{"amount":5085.83,"currency":"CNY"}}
     * config : {"prepaySettledInstalmentsCount":null,"expiryDate":"","overdueAdvanceParty":"risk","financialAssetsHeldInceptionDate":"2016-10-14","passDays":1,"graceDays":3,"valueDays":0,"overduePenaltyRate":0,"transferFeeRate":0,"payOffDaysLimit":null,"overdueAdvanceDays":null,"prepaymentPenaltyDays":null,"prepayPendingInstalmentsCount":null,"transferableStartDays":15,"transferableEndDays":10,"investProfitFeeRate":0,"investProfitFeeBeneficiaryParty":"p2p-investor-profit","financialAssetsHeldTerminationDate":"2017-04-10","redDays":999}
     * user : {"id":97071}
     * transferable : true
     * instalmentPolicy : {"type":"ONCE_PRINCIPAL_AND_INTEREST","interval":{"count":88,"intervalUnit":"DAY"},"numberOfInstalments":1,"annualRate":0.0686452717}
     */

    private boolean guaranteeable;
    /**
     * amount : 5085.83
     * currency : CNY
     */

    private RemainingAmountBean remainingAmount;
    private boolean mortgageable;
    private String status;
    private Object contract;
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
     * amount : 5085.83
     * currency : CNY
     */

    private TransferAmountBean transferAmount;
    /**
     * amount : 0
     * currency : CNY
     */

    private TransferFeeBean transferFee;
    /**
     * investmentAmount : {"amount":5085.83,"currency":"CNY"}
     */

    private InvestmentPolicyBean investmentPolicy;
    /**
     * prepaySettledInstalmentsCount : null
     * expiryDate :
     * overdueAdvanceParty : risk
     * financialAssetsHeldInceptionDate : 2016-10-14
     * passDays : 1
     * graceDays : 3
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
     * financialAssetsHeldTerminationDate : 2017-04-10
     * redDays : 999
     */

    private ConfigBean config;
    /**
     * id : 97071
     */

    private UserBean user;
    private boolean transferable;
    /**
     * type : ONCE_PRINCIPAL_AND_INTEREST
     * interval : {"count":88,"intervalUnit":"DAY"}
     * numberOfInstalments : 1
     * annualRate : 0.0686452717
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

    public Object getContract() {
        return contract;
    }

    public void setContract(Object contract) {
        this.contract = contract;
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

    public TransferAmountBean getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(TransferAmountBean transferAmount) {
        this.transferAmount = transferAmount;
    }

    public TransferFeeBean getTransferFee() {
        return transferFee;
    }

    public void setTransferFee(TransferFeeBean transferFee) {
        this.transferFee = transferFee;
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

    public static class TransferFeeBean {
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
    }

    public static class InvestmentPolicyBean {
        /**
         * amount : 5085.83
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
        private String expiryDate;
        private String overdueAdvanceParty;
        private String financialAssetsHeldInceptionDate;
        private int passDays;
        private int graceDays;
        private int valueDays;
        private int overduePenaltyRate;
        private int transferFeeRate;
        private Object payOffDaysLimit;
        private Object overdueAdvanceDays;
        private Object prepaymentPenaltyDays;
        private Object prepayPendingInstalmentsCount;
        private int transferableStartDays;
        private int transferableEndDays;
        private int investProfitFeeRate;
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

        public int getTransferFeeRate() {
            return transferFeeRate;
        }

        public void setTransferFeeRate(int transferFeeRate) {
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
         * count : 88
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
        return "TransferFor{" +
                "guaranteeable=" + guaranteeable +
                ", remainingAmount=" + remainingAmount +
                ", mortgageable=" + mortgageable +
                ", status='" + status + '\'' +
                ", contract=" + contract +
                ", catalog='" + catalog + '\'' +
                ", type='" + type + '\'' +
                ", remainingTransferDays=" + remainingTransferDays +
                ", canUseVoucherTag='" + canUseVoucherTag + '\'' +
                ", currentInvestmentAmount=" + currentInvestmentAmount +
                ", id=" + id +
                ", amount=" + amount +
                ", title='" + title + '\'' +
                ", transferableStartDays=" + transferableStartDays +
                ", transferAmount=" + transferAmount +
                ", transferFee=" + transferFee +
                ", investmentPolicy=" + investmentPolicy +
                ", config=" + config +
                ", user=" + user +
                ", transferable=" + transferable +
                ", instalmentPolicy=" + instalmentPolicy +
                '}';
    }
}
