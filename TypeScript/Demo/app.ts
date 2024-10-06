var message:string = "Hello World!"
console.log(message)

class Test {
	sayHello():void {
		console.log("Hello World")
	}
}
var obj = new Test()
obj.sayHello()
var num = 12
console.log(typeof num)

function addNumbers(...nums: number[]) {
	var i;
	var sum: number = 0
	for (i = 0; i < nums.length; i++) {
		sum = sum + nums[i];
	}
	console.log("和为：", sum)
}

addNumbers(1, 2, 3)
addNumbers(10, 10, 10, 10, 10)

var foo = (x: number) => 10 + x
console.log(foo(100))

interface ILoan { 
   interest:number 
} 
 
class AgriLoan implements ILoan { 
   interest:number 
   rebate:number 
   constructor(interest:number,rebate:number) { 
      this.interest = interest 
      this.rebate = rebate 
   } 
} 
var obj1 = new AgriLoan(10,1) 
console.log("利润为: " + obj1.interest + "，抽成为: " + obj1.rebate)
