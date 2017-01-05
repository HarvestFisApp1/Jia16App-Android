package com.jia16.bean;

import java.util.List;

/**
 * 风险评测的java bean
 */
public class Risk {
    /**
     * required : true
     * id : 1
     * title : 您对互联网的使用经验如何：
     * options : [{"value":1,"label":"生活必需"},{"value":2,"label":"时常会用"},{"value":3,"label":"几乎不会使用"}]
     */

    private boolean required;
    private int id;
    private String title;
    /**
     * value : 1
     * label : 生活必需
     */

    private List<OptionsBean> options;

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
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

    public List<OptionsBean> getOptions() {
        return options;
    }

    public void setOptions(List<OptionsBean> options) {
        this.options = options;
    }

    public static class OptionsBean {
        private int value;
        private String label;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return "OptionsBean{" +
                    "value=" + value +
                    ", label='" + label + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Risk{" +
                "required=" + required +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", options=" + options +
                '}';
    }
}
