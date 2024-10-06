var message = "Hello World!";
console.log(message);
var Test = /** @class */ (function () {
    function Test() {
    }
    Test.prototype.sayHello = function () {
        console.log("Hello World");
    };
    return Test;
}());
var obj = new Test();
obj.sayHello();
var num = 12;
console.log(typeof num);
function addNumbers() {
    var nums = [];
    for (var _i = 0; _i < arguments.length; _i++) {
        nums[_i] = arguments[_i];
    }
    var i;
    var sum = 0;
    for (i = 0; i < nums.length; i++) {
        sum = sum + nums[i];
    }
    console.log("和为：", sum);
}
addNumbers(1, 2, 3);
addNumbers(10, 10, 10, 10, 10);
var foo = function (x) { return 10 + x; };
console.log(foo(100));
var AgriLoan = /** @class */ (function () {
    function AgriLoan(interest, rebate) {
        this.interest = interest;
        this.rebate = rebate;
    }
    return AgriLoan;
}());
var obj1 = new AgriLoan(10, 1);
console.log("利润为 : " + obj1.interest + "，抽成为 : " + obj1.rebate);
