package ale.rains.demo.pattern;

/**
 * 建造者模式
 * 建造者模式是一种创建型设计模式，它允许您通过一步一步地构建复杂对象来创建不同类型的对象。
 * 它使用一个建造者类来封装对象的创建过程并将其分解为多个简单的步骤。
 * 这使得您可以通过更改这些步骤来创建不同类型的对象。
 */
public class BuilderPattern {
    /*
    public class Car {
        private String make;
        private String model;
        private int year;
        private String engine;
        private int seats;
        public Car(String make, String model, int year, String engine, int seats) {
            this.make = make;
            this.model = model;
            this.year = year;
            this.engine = engine;
            this.seats = seats;
        }
        // ... getter setter ... //
    }
    public class CarBuilder {
        private String make;
        private String model;
        private int year;
        private String engine;
        private int seats;
        public CarBuilder setMake(String make) {
            this.make = make;
            return this;
        }
        public CarBuilder setModel(String model) {
            this.model = model;
            return this;
        }
        public CarBuilder setYear(int year) {
            this.year = year;
            return this;
        }
        public CarBuilder setEngine(String engine) {
            this.engine = engine;
            return this;
        }
        public CarBuilder setSeats(int seats) {
            this.seats = seats;
            return this;
        }
        public Car build() {
            return new Car(make, model, year, engine, seats);
        }
    }
    // 演示代码
    public static void main(String[] args) {
        CarBuilder carBuilder = new CarBuilder();
        Car car = carBuilder.setMake("BWM")
                .setModel("x5")
                .setYear(2023).
                setEngine("2.0T")
                .setSeats(5)
                .build();
    }
    */
}
