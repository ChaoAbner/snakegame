### 为什么使用泛型

​		泛型程序设计意味着编写的代码可以被不同类型的对象所重用。

### <>里面大写字母的含义

- `T`（或者用临近字母`U`和`S`）代表任意类型。
- `E`表示集合的元素类型。
- `K`表示表的键的类型。
- `V`表示表的值的类型。

### 泛型类

```java
public class Pair<T> {
    private T first;
    private T second;	//指定属性类型
    
    public Pair(T first, T second) {
        this.first = first;
        this.second = second;
    }
    
    public setAll(T first, T second) {
        this.first = first;
        this.second = second;
    }
    
    public getFirst(){
        return first;
    }
}
```

​		Pair类引入类型变量，用<>框起来存放在类名后面。

​		泛型类也可以有多个类型变量。

```java
public class Pair<T, U>{...}
```

​		泛型类可以看作普通类的工厂。

### 泛型方法

```java
public class Test{
   public static <T> T max(T[] str) {
	/**
	* 方法体
	*/
	} 
}

```

​		声明类型参数的<T>放在修饰符后面，返回类型前面。

​		泛型方法调用：

```java
String[] names = {"John","Chao","Biao","Yu"};

// 当max方法在Test类中时
String max = Test.<String>max(names);	// 可以添加类型变量
String max = Test.max(names);			// 编译器自动是被类型变量

```

### 类型变量的限定

​		有时我们不想让类型变量的可选范围过大，而是有所限定的某种类型。比如我们需要比较出数组中元素的最小值。

```java
class ArrayAlg{
	public static <T> T min(T[] a) {
		if(a == null || a.length == 0) {
			return null;
		}
		T smallest = a[0];
		for(int i = 1; i < a.length; a ++){
			if(smallest.compareTo(a[i]) > 0){
				smallest = a[i];
			}
		}
		return smallest;
	}
}
```

​		在这种情况下我们需要考虑这个类型变量所属的类是否具有`compareTo`方法，或者说是否实现了`Comparable`接口。这个问题的解决方案就是让类型变量继承`Comparable`接口，以限定它的范围是只能接受实现了`Comparable`接口的类型。

```java
public static <T extends Comparable> T min(T[] a) {...}
```

​		这是，若使用`Rectangle`等未实现`Comparable`接口的类去调用`min`的时候，便会发生编译错误。

​		同时，类型变量也可以有多个限定，限定类型用`&`分隔。

```java
public static <T extends Comparable & Serializable> T min(T[] a) {...}
```

### 泛型代码和虚拟机

​		虚拟机没有泛型类型对象 —— 所有的对象属于普通类。

​		定义一个泛型类型，就自动提供了一个相应的原始类型。原始类型的名字即使删去类型参数后的泛型类型名。

​		擦除类型变量，并替换成限定类型，无限定类型则替换为（Object）。

​		例如Pair<T>擦除后的原始类型是Object，Pair<String>或Pair<Rectangle>被擦除后的原始类型则变为Pair。

#### 翻译泛型表达式

​		当程序调用泛型方法时，如果擦除返回类型，编译器插入强转类型。例如

```java
Pair<Employee> buddies = ...;
Emplyee buddy = buddies.getFirst();
```

​		擦除getFirst()方法返回值的类型后，返回的为Object类型，编译器自动插入Employee的强制类型转换。

也就是说，编译器把这个方法拆成两个步骤

- 对原始方法Pair.getFirst的调用
- 将返回的Object类型转为Empoyee类型

#### 翻译泛型方法

```java
public static <T extends Comparable> T min(T[] a) {...}

// 擦除后,只留下限定类型
public static Comparable min(Comparable[] a) {...}

```

##### 桥方法

。。。。

​	需要记住的java泛型转换的事实：

- 虚拟机中没有泛型，只有普通的类和方法。
- 所有的类型参数都用它们的限定类型替换。
- 桥方法被合成来保证多态。
- 为保持类型安全性，必要时插入强制类型转换。

### 约束与局限性

#### 不能用基本数据类型实例化类型参数

​		没有Pair<int>，只能用Pair<Integer>，原因是类型擦除，类型擦除后类型变为Object类型，Pair的实例域中含有Object类的域，Object不能使用储存int值。

#### 运行时类型查询只适用于原始类型

​		虚拟机中的对象总有一个非泛型类型，因此，类型查询只产生原始类型。下面代码仅仅测试a是否是任意类型的Pair。而不是String等等。

```java
if(a instanceof Pair<String>) {...}

if(a instanceof Pair<T>){...}

```

​		同理，getClass返回的结果也是返回原始类型。

```java
Pair<String> strPair = 	...;
Pair<Employee> employeePair = ...;

strPair.getClass() == employeePair.getClass()  // they are equal,都返回Pair.class

```

#### 参数化类型的数组不合法

​		不能声明一个参数化的数组，如：

```java
Pair<String> table = new Pair<String>[10];	//error

```

​		table类型擦除后，是Pair类型，可以将其转为Object类型。

```java
Object[] objArray = table;

```

​		数组能够记住它的元素类型，如果试图存入一个错误类型的元素，就会抛出ArrayStoreException异常。

```java
objArray[0] = "hello"; 	//error, 元素类型是Pair

```

#### 不能实例化类型变量

​		不能使用像`new T(...)`,  `new T[...]`或`T.Class`这样的表达式。

​		我们可以用反射调用Class.newInstance方法构建一个泛型对象。

```java
test = T.Class.newInstance(); // ERROR

```

​		T.Class是不合法的，必须使用下面的API来支配泛型对象

```java
public static <T> Pair<T> makePair(Class<T> cl) {
	try{
		return new Pair<T>(cl.newInstance(), cl.newInstance())
	} catch (Exception ex) {
		return null;
	}
}

```

​		可以按照下列的方法进行调用

```java
Pair<String> p = Pair.makePair(String.Class);

```

​		需要注意的是，Class本来就是一个泛型类，String.Class是一个Class<String>的实例，（实际上也是唯一实例），因此makePair方法可以判断出pair的类型。

​		不能构造一个泛型数组

```java
T[] mm = new T[2];		// ERROR

```

​		由于泛型擦除，上面的代码永远构建Object[2]的数组。

#### 泛型类的静态上下文中类型变量无效

​		不能在静态域或方法中引用类型变量。

```java
public class Sing<T> {
	private static <T> music;		// ERROR
	
	public static <T> getMusic() {	// ERROR
		if(music != null) {
			return music;
		}
	}
}

```

### 通配符类型

​		固定的泛型并不灵活，所以有时候我们可能会使用到通配符类型。比如`Pair<? extends Employee>`表示接受的类型只能是Employee本身或者它的子类。比如

```java
public static void printBuddied(Pair<? extends Employee> p) {...}

Pair<Manage> pm = new Pair<Manage>();
Pair<Employee> pe = new Pair<Employee>();
Pair<String> ps = new Pair<String>();
// printBuddied(ps);    Error

```

