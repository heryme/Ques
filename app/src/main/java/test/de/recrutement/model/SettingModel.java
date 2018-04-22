package test.de.recrutement.model;

import java.util.List;

/**
 * Created by Rahul Padaliya on 3/25/2017.
 */
public class SettingModel {
    String status_code;
    String message;
    List<SettingModelInner> SettingModelInnerList;

    public List<SettingModelInner> getSettingModelInnerList() {
        return SettingModelInnerList;
    }

    public void setSettingModelInnerList(List<SettingModelInner> settingModelInnerList) {
        this.SettingModelInnerList = settingModelInnerList;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class SettingModelInner {
        String id;
        String key;
        String value;
        String ip_address;
        String is_active;
        String is_deleted;
        String updated_at;
        String created_at;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getIp_address() {
            return ip_address;
        }

        public void setIp_address(String ip_address) {
            this.ip_address = ip_address;
        }

        public String getIs_active() {
            return is_active;
        }

        public void setIs_active(String is_active) {
            this.is_active = is_active;
        }

        public String getIs_deleted() {
            return is_deleted;
        }

        public void setIs_deleted(String is_deleted) {
            this.is_deleted = is_deleted;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }
}
