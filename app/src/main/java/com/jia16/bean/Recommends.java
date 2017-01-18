package com.jia16.bean;

/**
 * 推荐详情的bean
 */
public class Recommends {


    /**
     * id : 1990
     * recommendedRegisterTime : 2016-11-05
     * recommendedPhone : 158****9300
     * recommendedTotalInvest : {"amount":2000,"currency":"CNY"}
     * parentMidasUserId : 97071
     * createdAt : 2016-11-06
     * recommendedMidasUserId : 100360
     * parentCashCommission : {"amount":2,"currency":"CNY"}
     * recommendedRealName : **明
     * parentCashReward : {"amount":10,"currency":"CNY"}
     * recommendType : 邀请码
     */

    private int id;
    private String recommendedRegisterTime;
    private String recommendedPhone;
    /**
     * amount : 2000
     * currency : CNY
     */

    private RecommendedTotalInvestBean recommendedTotalInvest;
    private int parentMidasUserId;
    private String createdAt;
    private int recommendedMidasUserId;
    /**
     * amount : 2
     * currency : CNY
     */

    private ParentCashCommissionBean parentCashCommission;
    private String recommendedRealName;
    /**
     * amount : 10
     * currency : CNY
     */

    private ParentCashRewardBean parentCashReward;
    private String recommendType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecommendedRegisterTime() {
        return recommendedRegisterTime;
    }

    public void setRecommendedRegisterTime(String recommendedRegisterTime) {
        this.recommendedRegisterTime = recommendedRegisterTime;
    }

    public String getRecommendedPhone() {
        return recommendedPhone;
    }

    public void setRecommendedPhone(String recommendedPhone) {
        this.recommendedPhone = recommendedPhone;
    }

    public RecommendedTotalInvestBean getRecommendedTotalInvest() {
        return recommendedTotalInvest;
    }

    public void setRecommendedTotalInvest(RecommendedTotalInvestBean recommendedTotalInvest) {
        this.recommendedTotalInvest = recommendedTotalInvest;
    }

    public int getParentMidasUserId() {
        return parentMidasUserId;
    }

    public void setParentMidasUserId(int parentMidasUserId) {
        this.parentMidasUserId = parentMidasUserId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getRecommendedMidasUserId() {
        return recommendedMidasUserId;
    }

    public void setRecommendedMidasUserId(int recommendedMidasUserId) {
        this.recommendedMidasUserId = recommendedMidasUserId;
    }

    public ParentCashCommissionBean getParentCashCommission() {
        return parentCashCommission;
    }

    public void setParentCashCommission(ParentCashCommissionBean parentCashCommission) {
        this.parentCashCommission = parentCashCommission;
    }

    public String getRecommendedRealName() {
        return recommendedRealName;
    }

    public void setRecommendedRealName(String recommendedRealName) {
        this.recommendedRealName = recommendedRealName;
    }

    public ParentCashRewardBean getParentCashReward() {
        return parentCashReward;
    }

    public void setParentCashReward(ParentCashRewardBean parentCashReward) {
        this.parentCashReward = parentCashReward;
    }

    public String getRecommendType() {
        return recommendType;
    }

    public void setRecommendType(String recommendType) {
        this.recommendType = recommendType;
    }

    public static class RecommendedTotalInvestBean {
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

    public static class ParentCashCommissionBean {
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

    public static class ParentCashRewardBean {
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
        return "Recommends{" +
                "id=" + id +
                ", recommendedRegisterTime='" + recommendedRegisterTime + '\'' +
                ", recommendedPhone='" + recommendedPhone + '\'' +
                ", recommendedTotalInvest=" + recommendedTotalInvest +
                ", parentMidasUserId=" + parentMidasUserId +
                ", createdAt='" + createdAt + '\'' +
                ", recommendedMidasUserId=" + recommendedMidasUserId +
                ", parentCashCommission=" + parentCashCommission +
                ", recommendedRealName='" + recommendedRealName + '\'' +
                ", parentCashReward=" + parentCashReward +
                ", recommendType='" + recommendType + '\'' +
                '}';
    }
}
