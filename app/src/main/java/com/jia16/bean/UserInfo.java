package com.jia16.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huangjun on 16/8/15.
 */
public class UserInfo implements Serializable {


    /**
     * bankCards : [{"bankCode":"03080000","accountType":"INVESTMENT","links":[{"operation":"/users/6/bank-cards/1/operation"}],"id":1,"bankReservedPhone":"15927590731","cardNo":"6214830270786337"}]
     * isBind : false
     * active : true
     * type : CUSTOMER
     * uri : /users/6
     * certification : {"certifiedName":"李辉","certifiedIdentity":"420103198807035319"}
     * createdAt : 1470021823705
     * isChangeUserName : false
     * needDealPassword : false
     * lastLoginAt : 1471533053204
     * phone : 13212345678
     * riskCapacity : {"riskCapacityLevelText":"激进型","riskCapacityLevelCode":"RADICALIZATION"}
     * notifyDepositAgreement : true
     * id : 6
     * username : jsl1470021823577fw
     */

    private boolean isBind;
    private boolean active;
    private String type;
    private String uri;
    /**
     * certifiedName : 李辉
     * certifiedIdentity : 420103198807035319
     */

    private CertificationBean certification;
    private long createdAt;
    private boolean isChangeUserName;
    private boolean needDealPassword;
    private long lastLoginAt;
    private String phone;
    /**
     * riskCapacityLevelText : 激进型
     * riskCapacityLevelCode : RADICALIZATION
     */

    private RiskCapacityBean riskCapacity;
    private boolean notifyDepositAgreement;
    private int id;
    private String username;
    /**
     * bankCode : 03080000
     * accountType : INVESTMENT
     * links : [{"operation":"/users/6/bank-cards/1/operation"}]
     * id : 1
     * bankReservedPhone : 15927590731
     * cardNo : 6214830270786337
     */

    private List<BankCardsBean> bankCards;

    public boolean isIsBind() {
        return isBind;
    }

    public void setIsBind(boolean isBind) {
        this.isBind = isBind;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public CertificationBean getCertification() {
        return certification;
    }

    public void setCertification(CertificationBean certification) {
        this.certification = certification;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isIsChangeUserName() {
        return isChangeUserName;
    }

    public void setIsChangeUserName(boolean isChangeUserName) {
        this.isChangeUserName = isChangeUserName;
    }

    public boolean isNeedDealPassword() {
        return needDealPassword;
    }

    public void setNeedDealPassword(boolean needDealPassword) {
        this.needDealPassword = needDealPassword;
    }

    public long getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(long lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public RiskCapacityBean getRiskCapacity() {
        return riskCapacity;
    }

    public void setRiskCapacity(RiskCapacityBean riskCapacity) {
        this.riskCapacity = riskCapacity;
    }

    public boolean isNotifyDepositAgreement() {
        return notifyDepositAgreement;
    }

    public void setNotifyDepositAgreement(boolean notifyDepositAgreement) {
        this.notifyDepositAgreement = notifyDepositAgreement;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<BankCardsBean> getBankCards() {
        return bankCards;
    }

    public void setBankCards(List<BankCardsBean> bankCards) {
        this.bankCards = bankCards;
    }

    public static class CertificationBean {
        private String certifiedName;
        private String certifiedIdentity;

        public String getCertifiedName() {
            return certifiedName;
        }

        public void setCertifiedName(String certifiedName) {
            this.certifiedName = certifiedName;
        }

        public String getCertifiedIdentity() {
            return certifiedIdentity;
        }

        public void setCertifiedIdentity(String certifiedIdentity) {
            this.certifiedIdentity = certifiedIdentity;
        }
    }

    public static class RiskCapacityBean {
        private String riskCapacityLevelText;
        private String riskCapacityLevelCode;

        public String getRiskCapacityLevelText() {
            return riskCapacityLevelText;
        }

        public void setRiskCapacityLevelText(String riskCapacityLevelText) {
            this.riskCapacityLevelText = riskCapacityLevelText;
        }

        public String getRiskCapacityLevelCode() {
            return riskCapacityLevelCode;
        }

        public void setRiskCapacityLevelCode(String riskCapacityLevelCode) {
            this.riskCapacityLevelCode = riskCapacityLevelCode;
        }
    }

    public static class BankCardsBean {
        private String bankCode;
        private String accountType;
        private int id;
        private String bankReservedPhone;
        private String cardNo;
        /**
         * operation : /users/6/bank-cards/1/operation
         */

        private List<LinksBean> links;

        public String getBankCode() {
            return bankCode;
        }

        public void setBankCode(String bankCode) {
            this.bankCode = bankCode;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBankReservedPhone() {
            return bankReservedPhone;
        }

        public void setBankReservedPhone(String bankReservedPhone) {
            this.bankReservedPhone = bankReservedPhone;
        }

        public String getCardNo() {
            return cardNo;
        }

        public void setCardNo(String cardNo) {
            this.cardNo = cardNo;
        }

        public List<LinksBean> getLinks() {
            return links;
        }

        public void setLinks(List<LinksBean> links) {
            this.links = links;
        }

        public static class LinksBean {
            private String operation;

            public String getOperation() {
                return operation;
            }

            public void setOperation(String operation) {
                this.operation = operation;
            }
        }
    }
}
