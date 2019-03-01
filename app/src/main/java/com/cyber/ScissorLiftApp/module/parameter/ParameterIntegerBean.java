package com.cyber.ScissorLiftApp.module.parameter;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Heshuai
 * @version V1.0
 * @Package com.cyber.ScissorLiftApp.module.bean
 * @date 2019/1/18 14:05
 * @Description: 整数参数类
 * type 检查类型 0 只读,1 枚举,2 范围(范围仅支持成对的范围) 3 开关量
 */
public class ParameterIntegerBean implements IParameter , Parcelable {

    private String name;

    private String description;

    private Integer val;

    private String  measurementUnit;

    private Integer type;

    private List<Integer> limit;

    private Boolean enabled;

    public ParameterIntegerBean() {

    }

    @Override
    public String toString() {
        return "ParameterIntegerBean{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", val=" + val +
                ", measurementUnit='" + measurementUnit + '\'' +
                ", type=" + type +
                ", limit=" + limit +
                ", enabled=" + enabled +
                '}';
    }

    /**
     * @param name
     * @param description
     * @param val
     * @param measurementUnit 计量单位
     * @param type  检查类型 0 只读,1枚举,2范围(范围仅支持成对的范围)
     * @param limit 范围如"1,3,-1,0"表示[1,3],[-1,0]
     */
    public ParameterIntegerBean(String name, String description, Integer val, String measurementUnit, Integer type, int[] limit) {
        this.name = name;
        this.description = description;
        this.val = val;
        this.measurementUnit = measurementUnit;
        this.type = type;
        this.limit = new ArrayList<>();
        for(int i=0;i<limit.length;i++) this.limit.add(Integer.valueOf(limit[i]));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 0 只读,1 枚举,2 范围(范围仅支持成对的范围) 3 开关量
     */
    public Integer getVal() {
        return val;
    }

    public void setVal(Integer val) {
        this.val = val;
    }

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<Integer> getLimit() {
        return limit;
    }

    public void setLimit(List<Integer> limit) {
        this.limit = limit;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean passCheck() {
        switch (type) {
            case 0:
                return false;
            case 1:
                return limit.contains(val);
            case 2:
                for (int i = 0, sz = limit.size(); i < sz; i += 2)
                    if (between(val, limit.get(i), limit.get(i + 1))) return true;
                return false;
            default:
        }//
        return false;
    }


    /**
     * @param min
     * @param max
     * @return boolean  [min<=   val   <   =   max   ]
     */
    boolean between(Integer val, Integer min, Integer max) {
        return val.compareTo(max) <= 0 && val.compareTo(min) >= 0;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeValue(this.val);
        dest.writeString(this.measurementUnit);
        dest.writeValue(this.type);
        dest.writeList(this.limit);
        dest.writeValue(this.enabled);
    }

    protected ParameterIntegerBean(Parcel in) {
        this.name = in.readString();
        this.description = in.readString();
        this.val = (Integer) in.readValue(Integer.class.getClassLoader());
        this.measurementUnit = in.readString();
        this.type = (Integer) in.readValue(Integer.class.getClassLoader());
        this.limit = new ArrayList<Integer>();
        in.readList(this.limit, Integer.class.getClassLoader());
        this.enabled = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Creator<ParameterIntegerBean> CREATOR = new Creator<ParameterIntegerBean>() {
        @Override
        public ParameterIntegerBean createFromParcel(Parcel source) {
            return new ParameterIntegerBean(source);
        }

        @Override
        public ParameterIntegerBean[] newArray(int size) {
            return new ParameterIntegerBean[size];
        }
    };
}
