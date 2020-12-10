# AOP（面向切面编程）

## 代理模式

### 静态代理

角色分析：

- 抽象角色：一般会使用借口或者抽象类来解决
- 真实角色：被代理的角色
- 代理角色：代理真实角色，代理真实角色后，一般会做一些附属操作
- 客户：访问代理对象的人

代理模式的好处：

- 可以使真实角色的操作更加纯粹！不用去关注一些公共的业务
- 公共业务交给代理角色，实现了业务的分工
- 公共业务发生扩展的时候，方便集中管理

缺点：

- 一个真实角色就会产生一个代理角色；代码量会翻倍，开发效率会变低

### 动态代理

- 动态代理和静态代理角色一样
- 动态代理的代理类是动态生成的，不是我们直接写好的。
- 动态代理分为两大类：基于接口的动态代理，基于类的动态代理
  - 基于接口——JDK动态代理
  - 基于类：cglib
  - java字节码实现：javasist

需要了解两个类：Proxy 代理    invocationHandler 调用处理程序

 #### InvocationHandler

动态代理的好处：

- 可以使真实角色的操作更加纯粹！不用去关注一些公共的业务
- 公共业务交给代理角色，实现了业务的分工
- 公共业务发生扩展的时候，方便集中管理
- 一个动态代理类代理的是一个接口，一般就是对应一类业务
- 一个动态代理类可以代理多个类，只要是实现了同一个接口即可

```java
// 自动生成代理类！
public class ProxyInvocationHandler implements InvocationHandler {
  
  // 被代理的接口
  private Object target;
  
  public void setTarget(Object target) {
    this.target = target;
  }
  
  // 生成得到代理类
  public Object getProxy() {
    return Proxy.newProxyInstance(this.getClass().getClassLoader(),target.getClass().getInterfaces(),this);
  }
  
  // 处理代理实例，并返回结果
  public Object invoke(Object proxy,Method method,Object[] args) throws Throwable {
    Object result = method.invoke(target,args);
    return result;
  }
}
```



## AOP在Spring中的作用

**提供声明式事务；允许用户自定义切面**

- 横切关注点：跨越应用程序多个模块的方法或者功能。即是，与业务逻辑无关，但是需要关注的部分，就是横切关注点。如日志，安全，缓存，事务等等
- 切面（ASPECT）：横切关注点 被模块化 的特殊对象。即，它是一个类
- 通知（Advice）：前面必须要完成的工作。即，是类中的一个方法
- 目标（Target）：被通知对象
- 代理（Proxy）：向目标对象应用通知之后创建的对象
- 切入点（PointCut）：切面通知 执行的“地点”的定义
- 连接点（JointPoint）：与切入点匹配的执行点

### 使用Spring实现AOP

```xml
<dependency>
  	<groupId>org.aspectj</groupId>
	  <artifactId>aspectjweaver</artifactId>
	  <version>1.9.4</version>
</dependency>
```

方式一：使用Spring的API接口【主要是SpringAPI接口实现】

方式二：自定义类实现【主要是切面定义】

方式三：使用注解实现