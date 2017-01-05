package com.jia16.bean;

/**
 * 申请中的bean
 */
public class ApplyFor {


    /**
     * id : 499
     * user : {"id":15}
     * title : 企业借款标的20161124
     * catalog : JIASHI_V13
     * instalmentPolicy : {"annualRate":0.05,"interval":{"count":52,"intervalUnit":"DAY"},"numberOfInstalments":1,"type":"ONCE_PRINCIPAL_AND_INTEREST"}
     * subjectPid : 0
     */

    private SubjectBean subject;
    /**
     * currency : CNY
     * amount : 20000.0
     */

    private InvestmentAmountBean investmentAmount;
    /**
     * subject : {"id":499,"user":{"id":15},"title":"企业借款标的20161124","catalog":"JIASHI_V13","instalmentPolicy":{"annualRate":0.05,"interval":{"count":52,"intervalUnit":"DAY"},"numberOfInstalments":1,"type":"ONCE_PRINCIPAL_AND_INTEREST"},"subjectPid":0}
     * investmentAmount : {"currency":"CNY","amount":20000}
     * numInstal : null
     * investAt : 1480315975962
     * status : FUNDED
     * id : 779
     */

    private Object numInstal;
    private long investAt;
    private String status;
    private int id;

    public SubjectBean getSubject() {
        return subject;
    }

    public void setSubject(SubjectBean subject) {
        this.subject = subject;
    }

    public InvestmentAmountBean getInvestmentAmount() {
        return investmentAmount;
    }

    public void setInvestmentAmount(InvestmentAmountBean investmentAmount) {
        this.investmentAmount = investmentAmount;
    }

    public Object getNumInstal() {
        return numInstal;
    }

    public void setNumInstal(Object numInstal) {
        this.numInstal = numInstal;
    }

    public long getInvestAt() {
        return investAt;
    }

    public void setInvestAt(long investAt) {
        this.investAt = investAt;
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

    public static class SubjectBean {
        private int id;
        /**
         * id : 15
         */

        private UserBean user;
        private String title;
        private String catalog;
        /**
         * annualRate : 0.05
         * interval : {"count":52,"intervalUnit":"DAY"}
         * numberOfInstalments : 1
         * type : ONCE_PRINCIPAL_AND_INTEREST
         */

        private InstalmentPolicyBean instalmentPolicy;
        private int subjectPid;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
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

        public InstalmentPolicyBean getInstalmentPolicy() {
            return instalmentPolicy;
        }

        public void setInstalmentPolicy(InstalmentPolicyBean instalmentPolicy) {
            this.instalmentPolicy = instalmentPolicy;
        }

        public int getSubjectPid() {
            return subjectPid;
        }

        public void setSubjectPid(int subjectPid) {
            this.subjectPid = subjectPid;
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
            private double annualRate;
            /**
             * count : 52
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
        }
    }

    public static class InvestmentAmountBean {
        private String currency;
        private double amount;

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
    }

    @Override
    public String toString() {
        return "ApplyFor{" +
                "subject=" + subject +
                ", investmentAmount=" + investmentAmount +
                ", numInstal=" + numInstal +
                ", investAt=" + investAt +
                ", status='" + status + '\'' +
                ", id=" + id +
                '}';
    }
}
