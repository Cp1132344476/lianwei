# Springboot



## Springboot自动装配原理

自动装配：

pom.xml

- Spring-boot-dependencies:核心依赖在父工程中！
- 我们在写或者引入一些springboot依赖的时候，不需要指定版本，就因为有这些版本仓库



启动器

```xml
<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter</artifactId>
		</dependency>
```

- 启动器说白了就是springboot的启动场景
- 比如spring-boot-starter-web，就会自动导入web环境所有的依赖
- springboot会将所有的功能场景，都变成一个个启动器
- 要是用什么功能，就只需要找到相应的启动器就可以了`starter`

主程序：

```java
// 标注这个类是一个springboot的应用 启动类下的所有资源被导入
@SpringBootApplication
public class JavaPoiApplication {
	public static void main(String[] args) {
    // 将springboot应用启动
		SpringApplication.run(JavaPoiApplication.class, args);
	}
}
```

### 注解

- ```java
  @SpringBootconfiguration		springboot的配置
  		@Configuration					Spring的配置类
  		@Component							说明这是一个Spring的组件
  
  @EnableAutoConfiguration				自动配置
  		@AutoConfigurationPackage		自动配置包
  				@Import(AutoConfigurationPackage.Registra.class)	自动配置 包注册
  		@Import(AutoConfigurationImportSelector.class)	自动导入选择
  		
  
  // 获取所有的配置
  List<String> configurations = getCandidateConfigurations(annotationMetadata,attributes)
  ```

```java
// 获取候选的配置
protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes) {
		List<String> configurations = SpringFactoriesLoader.loadFactoryNames(getSpringFactoriesLoaderFactoryClass(),
				getBeanClassLoader());
		Assert.notEmpty(configurations, "No auto configuration classes found in META-INF/spring.factories. If you "
				+ "are using a custom packaging, make sure that file is correct.");
		return configurations;
	}

protected Class<?> getSpringFactoriesLoaderFactoryClass() {
		return EnableAutoConfiguration.class;
	}
```

META-INF/spring.factories：自动配置的核心文件



结论：springboot所有的自动配置都在启动的时候扫描并且加载：`spring.factories` 所有的自动配置类都在这里，但是不一定生效，要判断条件是否成立，只要导入对应的start，就有对应的启动器了，有了启动器，我们自动装配就会生效，然后就配置成功

1. springboot在启动的时候，从类路径下/META-INF/spring.factories获取指定的值；
2. 将这些自动配置的类导入容器，自动配置类就会生效
3. 以前需要手动配置的东西，现在springboot会解决
4. 整合javaee，解决方案和自动配置的东西都在spring-boot-autoconfigure-2.2.0.RELEASE.jar这个包下
5. 它会把所有需要导入的组件，以类名的方式返回，这些组件就会被添加到容器
6. 容器中也会存在很多的xxxAutoConfiguration的文件（@Bean），就是这些类给容器中导入了这个场景需要的所有的组件；并自动配置，@Configuration
7. 有了自动配置类，免去手动编写配置文件的工作。

### 启动

SpringApplication 这个类主要做了四件事情

1. 推断应用类型是普通的项目还是web项目
2. 查找并加载所有可用的初始化器，设置到initializers属性中
3. 找出所有的应用程序监听器，设置到listeners属性中
4. 推断并设置main方法的定义类，找到运行的主类



## 配置文件

### @ConfigurationProperties(prefix="")

将配置文件中配置的每一个属性的值，映射到这个组件中；告诉springboot将本类中的所有属性和配置文件中相关的配置进行绑定

参数prefix = "xxxx"：将配置文件中xxxx下面的所有属性一一对应

只有这个组件是容器中的组件，才能使用容器提供的@ConfigurationProperties功能

### @Validated 

JSR303数据校验



- ConfigurationProperties 主需要写一次就可以了，value则需要每个字段添加
- 松散绑定：比如yml中写的last-name，这个和lastName是一样的，后面跟着的字母默认是大写的。这就是松散绑定
- JSR303数据校验，就是可以在字段增加一层过滤器验证，可以保证数据的合法性
- 复杂类型封装，yml中可以封装对象，使用@Value不支持

结论：

- 配置yml和配置properties都可以获取到值。
- 如果在某个业务中，主需要配置文件中的某个值，可以使用一下@Value
- 如果专门编写了一个JavaBean来和配置文件进行映射，就直接使用@ConfigurationProperties

## 静态资源

1. 在springboot，可以使用一下方式处理静态资源
   - webjars 		localhost:8080/webjars/
   - Public,static,/**,resources       localhost:8080/

2. 优先级：resources>static(默认)>public