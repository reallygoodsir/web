package org.cars.model;

    public class Car {

        private CarInfo carInfo;

        private Integer carPrice;

        private Integer carId;

        private String carType;

        public CarInfo getCarInfo() {
            return carInfo;
        }

        public void setCarInfo(CarInfo carInfo) {
            this.carInfo = carInfo;
        }

        public Integer getCarPrice() {
            return carPrice;
        }

        public void setCarPrice(Integer carPrice) {
            this.carPrice = carPrice;
        }

        public Integer getCarId() {
            return carId;
        }

        public void setCarId(Integer carId) {
            this.carId = carId;
        }

        public String getCarType() {
            return carType;
        }

        public void setCarType(String carType) {
            this.carType = carType;
        }

        @Override
        public String toString() {
            return "Car{" +
                    "carInfo=" + carInfo +
                    ", carPrice=" + carPrice +
                    ", carId=" + carId +
                    ", carType='" + carType + '\'' +
                    "}\n";
        }
    }

