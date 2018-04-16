package dingshi.com.hibook.bean.card;

import java.io.Serializable;
import java.util.List;

import dingshi.com.hibook.bean.Result;

/**
 * @author wangqi
 * @since 2017/12/26 14:55
 */

public class CardDetails extends Result {

    private JsonDataBean jsonData;

    public JsonDataBean getJsonData() {
        return jsonData;
    }

    public void setJsonData(JsonDataBean jsonData) {
        this.jsonData = jsonData;
    }

    public static class JsonDataBean implements Serializable {
        /**
         * id : 1
         * user_id : 514930342958731266
         * name : Jack
         * phone : 15055407293
         * avatar : http://testapi.linkbooker.com/files/cardavatar/
         * is_open : 2
         * sex : 2    性别
         * company : Jingwei
         * position : Android   职业
         * city : 10001
         * advantage  资源优势
         * email : 794149144@qq.com   邮箱
         * birthday : 2017-12-26   生日
         * address : Zhoupu dong   地址
         * introduce : My name is mmp  个人介绍
         * ident1 :
         * ident2 :
         * identify : 0
         * is_main : 1
         * created_at : 2017-12-26 17:41:44
         * updated_at : 2017-12-26 17:41:44
         * education : [{"id":1,"user_id":514930342958731266,"card_id":1,"start":"2017-12-26 18:14:31","end":"2017-12-26 18:14:31","college":"Huainan Normal University","major":"Computer Sience","level":2,"describe":"My name is MMP","created_at":"2017-12-26 17:41:44","updated_at":"2017-12-26 18:14:31"}]
         * work : [{"id":1,"user_id":514930342958731266,"card_id":1,"start":"2017-12-26 18:15:23","end":"2017-12-26 18:15:23","company":"Jingwei","position":"Android","describe":"My name is mmp","created_at":"2017-12-26 17:41:44","updated_at":"2017-12-26 18:15:23"}]
         */

        /**
         * 0：未申请，1：待确认，2：已同意
         */
        private int state;
        private String apply_id;
        private String id;
        private String user_id;
        private String name;
        private String phone;
        private String avatar;
        private int is_open;
        private int sex;
        private String company;
        private String position;
        private String city;
        private String advantage;
        private String email;
        private String birthday;
        private String address;
        private String introduce;
        private String ident1;
        private String ident2;
        private int identify;
        private int is_main;
        private String created_at;
        private String updated_at;
        private List<EducationBean> education;
        private List<WorkBean> work;

        public String getApply_id() {
            return apply_id;
        }

        public void setApply_id(String apply_id) {
            this.apply_id = apply_id;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getAdvantage() {
            return advantage;
        }

        public void setAdvantage(String advantage) {
            this.advantage = advantage;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getIs_open() {
            return is_open;
        }

        public void setIs_open(int is_open) {
            this.is_open = is_open;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getIdent1() {
            return ident1;
        }

        public void setIdent1(String ident1) {
            this.ident1 = ident1;
        }

        public String getIdent2() {
            return ident2;
        }

        public void setIdent2(String ident2) {
            this.ident2 = ident2;
        }

        public int getIdentify() {
            return identify;
        }

        public void setIdentify(int identify) {
            this.identify = identify;
        }

        public int getIs_main() {
            return is_main;
        }

        public void setIs_main(int is_main) {
            this.is_main = is_main;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public List<EducationBean> getEducation() {
            return education;
        }

        public void setEducation(List<EducationBean> education) {
            this.education = education;
        }

        public List<WorkBean> getWork() {
            return work;
        }

        public void setWork(List<WorkBean> work) {
            this.work = work;
        }

        public static class EducationBean implements Serializable {
            /**
             * id : 1
             * user_id : 514930342958731266
             * card_id : 1
             * start : 2017-12-26 18:14:31
             * end : 2017-12-26 18:14:31
             * college : Huainan Normal University    学校
             * major : Computer Sience
             * level : 2
             * describe : My name is MMP
             * created_at : 2017-12-26 17:41:44
             * updated_at : 2017-12-26 18:14:31
             */

            private String id;
            private String user_id;
            private String card_id;
            private String start;
            private String end;
            private String college;
            private String major;
            private int level;
            private String describe;
            private String created_at;
            private String updated_at;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getCard_id() {
                return card_id;
            }

            public void setCard_id(String card_id) {
                this.card_id = card_id;
            }

            public String getStart() {
                return start;
            }

            public void setStart(String start) {
                this.start = start;
            }

            public String getEnd() {
                return end;
            }

            public void setEnd(String end) {
                this.end = end;
            }

            public String getCollege() {
                return college;
            }

            public void setCollege(String college) {
                this.college = college;
            }

            public String getMajor() {
                return major;
            }

            public void setMajor(String major) {
                this.major = major;
            }

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }

            public String getDescribe() {
                return describe;
            }

            public void setDescribe(String describe) {
                this.describe = describe;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }
        }

        public static class WorkBean implements Serializable {
            /**
             * id : 1
             * user_id : 514930342958731266
             * card_id : 1
             * start : 2017-12-26 18:15:23
             * end : 2017-12-26 18:15:23
             * company : Jingwei
             * position : Android
             * describe : My name is mmp
             * created_at : 2017-12-26 17:41:44
             * updated_at : 2017-12-26 18:15:23
             */

            private String id;
            private String user_id;
            private String card_id;
            private String start;
            private String end;
            private String company;
            private String position;
            private String describe;
            private String created_at;
            private String updated_at;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getCard_id() {
                return card_id;
            }

            public void setCard_id(String card_id) {
                this.card_id = card_id;
            }

            public String getStart() {
                return start;
            }

            public void setStart(String start) {
                this.start = start;
            }

            public String getEnd() {
                return end;
            }

            public void setEnd(String end) {
                this.end = end;
            }

            public String getCompany() {
                return company;
            }

            public void setCompany(String company) {
                this.company = company;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public String getDescribe() {
                return describe;
            }

            public void setDescribe(String describe) {
                this.describe = describe;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }
        }
    }
}
