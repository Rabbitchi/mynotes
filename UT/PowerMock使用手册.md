## 1、概念介绍

单元测试（unit testing），是指对软件中的最小可测试单元进行检查和验证。单元是人为规定的最小的被测功能模块。

本文主要讲Java中的单元测试中的代码编写，一般最小的单元就是一个方法，设计测试场景（一些边界条件），看运行结果是否满足预期，修改了代码也能帮助验证是否影响了原有的逻辑。

## 2、常用的Java测试框架

Junit：是一个为Java编程语言设计的单元测试框架。

Mockito：允许程序员使用自动化的单元测试创建和测试模拟对象。

PowerMock：PowerMock利用自定义的类加载器和字节码操纵器，来确保静态方法的模拟、静态初始化的删除、函数构造、最终的类和方法以及私有方法。

实际上还有许多优秀的测试框架，这几种笔者比较常用，所以本文记录下使用方法。

## 3、Maven引入

相关的Maven依赖如下

```
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>3.9.0</version>
    <scope>test</scope>
</dependency>
 
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.13.2</version>
    <scope>test</scope>
</dependency>
 
<dependency>
    <groupId>org.powermock</groupId>
    <artifactId>powermock-module-junit4</artifactId>
    <version>2.0.9</version>
    <scope>test</scope>
</dependency>
 
<dependency>
    <groupId>org.powermock</groupId>
    <artifactId>powermock-api-mockito2</artifactId>
    <version>2.0.9</version>
    <scope>test</scope>
</dependency>
```

![image-20241027223431173](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20241027223431173.png)

## 5、常用注解和配置

@Mock：创建一个模拟的对象，类似于@Autowired，但不是真实的对象，是Mock对象，这个注解使用在类属性上

@InjectMocks：创建一个实例，其余用@Mock注解创建的mock将被注入到用该实例中，这个注解使用在类属性上

@RunWith：表示一个运行器，@RunWith(PowerMockRunner.class)表示指定用PowerMockRunner运行，这个注解使用在类上

@PowerMockIgnore：这个注解表示将某些类延迟到系统类加载器加载，解决一些类加载异常。（具体类加载异常实际中还未遇见，后续补充），这个注解在类和方法上使用

@PrepareForTest：这个注解告诉PowerMock为测试准备某些类，通常是那些需要字节码操作的类。这包括带有final、private、static或native方法的类，new一个对象时，需要特殊处理（见下面的whenNew），这个注解在类和方法上使用

@Test：@Test修饰的public void方法可以作为测试用例运行。Junit会构造一个新的类实例，然后调用所有加了@Test的方法，方法执行过程中的任何异常，都会被判定为测试用例执行失败。

@Before：@Before注解的方法中的代码会在@Test注解的方法中首先被执行。可以做一些公共的前置操作：加入一些申请资源的代码：申请数据库资源，申请io资源，申请网络资源，new一些公共的对象等等。

@After：@After注解的方法中的代码会在@Test注解的方法中首先被执行。可以做一些公共的后置操作，如关闭资源的操作。

注：可以查看注解上的注释，了解其大致用法。



## 6、常规用法

下面给一个使用上述所有注解的简单例子：

```
import org.example.dto.CallbackDTO;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
 
import java.io.File;
import java.util.Map;
 
@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"org.xml.*", "javax.xml.*"})
@PrepareForTest({File.class, MessageCardService.class})
public class MessageCardServiceTest {
 
    @InjectMocks
    MessageCardService messageCardService;
 
    @Mock
    SendService sendService;
 
    @Before
    public void setUp() {
        System.out.println("前置操作");
    }
 
    @After
    public void after() {
        System.out.println("后置操作");
    }
 
    @Test
    public void sendMessage() {
        PowerMockito.when(sendService.getMsg()).thenReturn("test");
        Map map = messageCardService.updateMessageCard(new CallbackDTO());
        Assert.assertEquals(map.get("key"), null);
    }
}
```

被单元测试的方法源码如下：

```
@Service
public class MessageCardService {
 
    @Autowired
    private SendService sendService;
 
    public String testReturnValue(String param) {
        //调用其他类的方法
        param = param + "-" + "newValue";
        String msg = sendService.getMsg(param);
        return msg;
    }
 
    public String testTimes(String param, Integer times) {
        //调用其他类的方法
        String msg = "";
        for (int i = 0; i < times; i++) {
            msg = sendService.getMsg(param);
        }
        return msg;
    }
 
    public void testNewObject() {
        //new一个文件和日期
        File file = new File("path");
        Date now = new Date();
        System.out.println("date:" + now + ",length:" + file.length());
    }
 
    public void testStatic(String param) {
        //调其他类的静态方法
        String s = SendService.sendMsg(param);
        System.out.println("static result:" + s);
    }
 
    public void testPrivate(String param) {
        //调用自身的私有方法
        String msgHello = msgHello(param);
        System.out.println("msgHello:" + msgHello);
    }
 
    public void testVoid(String param) {
        //调用其他类的无返回值的方法
        sendService.printMsg(param);
    }
 
    private String msgHello(String param) {
        return param;
    }
    
}
```

```
@Service
public class SendService {
 
    public String getMsg(String msg) {
        return msg;
    }
 
    public void printMsg(String param) {
        System.out.println(param);
    }
 
    public static String sendMsg(String param) {
        return param;
    }
}
```

### 6.1、when().thenReturn()

当调用mock对象的方法时，根据入参条件，返回指定的值。只调用一次thenReturn()，则调用mock方式时始终返回相同的值；如果多次thenReturn()，可以在调用同一方法时，根据调用次数返回不同的值，如下：

```java
/**
 * Mock普通方法的返回值
 * 使用场景：当方法入参为某一值或者任何值时，返回一个模拟的结果
 */
@Test
public void testWhen() {
    //mock方法sendService.getMsg()的返回值，当入参为test时，第一次返回值为newValue，第二次返回new2Value
    PowerMockito.when(sendService.getMsg("test")).thenReturn("newValue").thenReturn("new2Value");
 
    //运行需要测试的实际方法
    String result = messageCardService.testReturnValue("test");
 
    //比较预期结果
    Assert.assertEquals("test-newValue", result);
}
```

### 6.2、whenNew().thenReturn()和verifyNew()

当需要new一个对象时，可以根据条件返回一个mock对象，下面的示例，当new File()时，如果入参是path，则返回mock的文件对象。注意：需要new 一个mock对象时，需要将 被测试类（@InjectMocks修饰的类）放入到@PrepareForTest注解中。

示例代码如下：

```java
/**
 * Mock new一个对象，并验证实例化次数是否满足预期
 * 使用场景：
 * 1、有时候，被测试方法里面需要new一些对象，为了让对象可控就可以采用whenNew的方式
 * 2、验证new的对象个数是否符合预期，可以采用verifyNew
 */
@Test
public void testWhenNewAndVerifyNew() throws Exception {
 
    //Mock new的File对象
    File mockFile = PowerMockito.mock(File.class);
    PowerMockito.when(mockFile.exists()).thenReturn(true);
    PowerMockito.when(mockFile.isFile()).thenReturn(true);
    PowerMockito.when(mockFile.length()).thenReturn(100L);
    PowerMockito.whenNew(File.class).withArguments("path").thenReturn(mockFile);
 
    //mock new的Date对象，可以定义一个类变量或者实际的对象，从而得到一个固定的结果
    Date mockDate = new Date();
    PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(mockDate);
 
    //运行需要测试的实际方法
    messageCardService.testNewObject();
 
    //验证是否mock new了一个日期实例（注意这里只能验证预期的对象创建，就是whenNew的对象次数，不是验证真实的对象new了几次）
    PowerMockito.verifyNew(Date.class, Mockito.times(1)).withNoArguments();
    //验证是否mock new了一个文件实例
    PowerMockito.verifyNew(File.class, Mockito.times(1)).withArguments("path");
 
}
```

注意：verifyNew()只能验证预期的对象创建，就是whenNew的对象次数，不是真实的对象创建次数，即如果该对象没有被mock，而是真实的创建对象，则次数不会被统计。

verifyNew()时需要将@InjectMocks修饰的类放入@PrepareForTest注解中，如下图：

![img](https://img2023.cnblogs.com/blog/2810288/202303/2810288-20230307204829558-1903411177.png)

### 6.3、Mockito.verify()

- 验证中间过程的方法入参是否符合预期，例如中间某个方法的入参经过了计算得到，则可以验证此入参是否符合预期

```java
/**
 * 验证中间过程的方法入参是否符合预期
 * 使用场景：被测试方法中有一个方法的入参是经过一系列运算得到，可以用于验证入参的运算逻辑是否符合预期
 */
@Test
public void testVerifyParam() {
    //mock方法sendService.getMsg()的返回值，当入参为test时，返回值为newValue
    PowerMockito.when(sendService.getMsg("test")).thenReturn("newValue");
 
    //运行需要测试的实际方法
    messageCardService.testReturnValue("test");
 
    //验证sendService.getMsg(string s)入参是否是test-newValue
    Mockito.verify(sendService).getMsg("test-newValue");
}
```

- 验证中间过程的方法被调用的次数是否符合预期

```java
/**
 * 验证中间过程的方法被调用的次数
 * 使用场景：当存在循环调用某个方法时，可以验证方法被调用的次数是否符合预期
 */
@Test
public void testVerifyTimes() {
    //mock方法sendService.getMsg()的返回值，当入参为test时，返回值为newValue
    PowerMockito.when(sendService.getMsg(Mockito.anyString())).thenReturn("newValue");
 
    //运行需要测试的实际方法
    messageCardService.testTimes("test", 2);
 
    //验证sendService.getMsg(string s)被调用次数是否为2次
    Mockito.verify(sendService, Mockito.times(2)).getMsg(Mockito.anyString());
}
```

### 6.4、PowerMockito.doNothing().when()

mock无返回值的方法

```java
/**
 * mock无返回值的方法（返回值为void）
 * 使用场景：比如有些操作是一个异步操作，无返回值，那么执行测试代码时，就可以采用doNothing
 */
@Test
public void testReturnVoid() {
    //mock一个无返回值的普通方法
    PowerMockito.doNothing().when(sendService).printMsg("test");
 
    //运行需要测试的实际方法
    messageCardService.testVoid("test");
}
```

### 6.5、PowerMockito.method()和Whitebox.invokeMethod()

mock私有方法，有两种方式，代码如下：

方式一：

```java
/**
 * mock私有方法
 */
@Test
public void testPrivate() throws InvocationTargetException, IllegalAccessException {
    //@InjectMocks注入MessageCardService，指定私有方法
    Method msgHello = PowerMockito.method(MessageCardService.class, "msgHello", String.class);
 
    //调用私有方法
    Object result = msgHello.invoke(messageCardService, "hello");
 
    //比较预期结果
    Assert.assertEquals("hello", result);
}
```

```
/**
 * mock私有方法2
 */
@Test
public void testPrivate2() throws Exception {
    //Whitebox 调用私有方法
    Object result = Whitebox.invokeMethod(messageCardService, "msgHello", "hello");
 
    //比较预期结果
    Assert.assertEquals("hello", result);
}
```

### 6.6、PowerMockito.mockStatic()和verifyStatic()

mock静态方法，需要注意将调用的静态方法的类放入@PrepareForTest注解，如下图：

![img](https://img2023.cnblogs.com/blog/2810288/202303/2810288-20230307204200721-722403019.png)

```
/**
 * mock静态方法
 * 使用场景：
 * 1、mock静态方法使用mockStatic
 * 2、需要验证静态方法的调用次数，可以使用verifyStatic，后面紧跟需要验证的静态方法
 */
@Test
public void testStaticAndVerifyStatic() {
    //mock有返回值的静态方法
    PowerMockito.mockStatic(SendService.class);
    PowerMockito.when(SendService.sendMsg("test")).thenReturn("newValue");
 
    //运行需要测试的实际方法
    messageCardService.testStatic("test");
 
    //验证静态方法的执行次数
    PowerMockito.verifyStatic(SendService.class, Mockito.times(1));
    SendService.sendMsg("test");
}
```

### 6.7、PowerMockito.verifyPrivate()

验证私有方法调用次数，被测试方法调用自身的私有方法，有时候需要统计次数，则可以采用verifyPrivate()

注意：只有spy的对象才能验证私有方法的调用次数，mock的对象不行。doReturn和thenReturn的区别在于，doReturn不会进入到方法里面直接返回，thenReturn会先走方法再返回

```
/**
 * 验证被测试方法调用的私有方法次数
 */
@Test
public void testVerifyPrivate() throws Exception {
    //spy一个对象
    MessageCardService spy = PowerMockito.spy(new MessageCardService());
 
    //mock私有方法
    PowerMockito.doReturn("hello").when(spy, "msgHello", "test");
 
    //运行需要测试的实际方法
    spy.testPrivate("test");
 
    //验证
    PowerMockito.verifyPrivate(spy, Mockito.times(1)).invoke("msgHello", "test");
}
```

### 8.1、Mock无返回值的静态方法

mock静态方法，需要注意将调用的静态方法的类放入@PrepareForTest注解，如下图：

![img](https://img2023.cnblogs.com/blog/2810288/202303/2810288-20230307204200721-722403019.png)

```
方式一：

PowerMockito.mockStatic(SendService.class);
PowerMockito.doNothing().when(SendService.class,"methodName",param1,param2);
 方式二：

PowerMockito.mockStatic(SendService.class);
PowerMockito.doNothing().when(SendService.class);
SendService.dealMsg(msgDto,"test msg");
第二种方式，在方法调用时，入参如果用到any()匹配，可能会抛出 InvalidUseOfMatcherException，可以在方法入参上加参数匹配器，如下：

PowerMockito.mockStatic(SendService.class);
PowerMockito.doNothing().when(SendService.class);
SendService.dealMsg(ArgumentMatchers.eq(msgDto),ArgumentMatchers.eq("test msg"));
方式三（补充）：

其实mock静态无返回值的方法，只需要将静态方法的类放入@PrepareForTest注解，然后mockStatic()对应的类即可，不需要显示的mock其方法，如下：

PowerMockito.mockStatic(SendService.class);

```

![image-20241027223905714](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20241027223905714.png)

### 以下是个人使用中得一个注意点：

![image-20241027224109011](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20241027224109011.png)

这个导入的包一定要对!