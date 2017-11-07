package com.iplay.feastbooking.gson.order;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Guyuhan on 2017/10/28.
 */

public class OrderListItem {

    public List<Content> content;

    public static class Content implements Serializable{

        public String banquetHall;

        public String contact;

        public String date;

        public String hotel;

        public int id;

        public String orderStatus;

        public List<String> roleInOrder;

        public int tables;

        private boolean isCustomer = false;
        private boolean isRecommander = false;
        private boolean isManager = false;
        private boolean isInit = false;

        public synchronized boolean getContact(){
            if(!isInit && roleInOrder != null){
                for(int i = 0; i< roleInOrder.size(); i++){
                    if(roleInOrder.get(i).equals("CUSTOMER")){
                        isCustomer = true;
                    }
                    if(roleInOrder.get(i).equals("RECOMMENDER")){
                        isRecommander = true;
                    }
                    if(roleInOrder.get(i).equals("MANAGER")){
                        isManager = true;
                    }
                }
            }
            isInit = true;
            return isCustomer;
        }

        public synchronized boolean isRecommander(){
            if(!isInit && roleInOrder != null){
                for(int i = 0; i< roleInOrder.size(); i++){
                    if(roleInOrder.get(i).equals("CUSTOMER")){
                        isCustomer = true;
                    }
                    if(roleInOrder.get(i).equals("RECOMMENDER")){
                        isRecommander = true;
                    }
                    if(roleInOrder.get(i).equals("MANAGER")){
                        isManager = true;
                    }
                }
            }
            isInit = true;
            return isRecommander;
        }

        public synchronized boolean isManager(){
            if(!isInit && roleInOrder != null){
                for(int i = 0; i< roleInOrder.size(); i++){
                    if(roleInOrder.get(i).equals("CUSTOMER")){
                        isCustomer = true;
                    }
                    if(roleInOrder.get(i).equals("RECOMMENDER")){
                        isRecommander = true;
                    }
                    if(roleInOrder.get(i).equals("MANAGER")){
                        isManager = true;
                    }
                }
            }
            isInit = true;
            return isManager;
        }

        @Override
        public String toString() {
            return "Content{" +
                    "banquetHall='" + banquetHall + '\'' +
                    ", contact='" + contact + '\'' +
                    ", date='" + date + '\'' +
                    ", hotel='" + hotel + '\'' +
                    ", id=" + id +
                    ", orderStatus='" + orderStatus + '\'' +
                    ", roleInOrder=" + roleInOrder +
                    ", tables=" + tables +
                    '}';
        }
    }

}
