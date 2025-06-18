# 餐急送外卖平台 (Quick Eats)

## 项目简介

餐急送外卖平台是一个基于Spring Boot 3.0的现代化外卖管理系统，采用前后端分离架构，为餐厅提供完整的外卖业务解决方案。系统包含后台管理端和用户端两个主要模块，支持菜品管理、订单处理、用户管理等核心功能。

## 技术架构

### 技术栈

- **后端框架**: Spring Boot 3.0.2
- **数据库**: MySQL 8.0
- **缓存**: Redis
- **ORM框架**: MyBatis-Plus 3.5.3.2
- **安全认证**: JWT (JSON Web Token)
- **API文档**: Knife4j 4.5.0 (Swagger)
- **工具库**: Hutool 5.8.20, Apache Commons Lang3
- **文件存储**: MinIO
- **Excel处理**: Apache POI 5.0.0
- **构建工具**: Maven
- **Java版本**: JDK 17

### 项目结构

```
hu-quick-eats/
├── quick-server/          # 服务端模块 (入口项目)
├── quick-pojo/           # 实体类模块 (数据传输对象)
├── quick-common/         # 公共模块 (工具类、配置等)
├── pom.xml              # 父工程POM配置
└── lombok.config        # Lombok配置文件
```

## 模块详解

### 1. quick-server (服务端模块)

**职责**: 应用程序入口，包含所有业务逻辑实现

**主要包结构**:
```
com.zmx.quickserver/
├── QuickServerApplication.java    # 启动类
├── controller/                    # 控制器层
│   ├── admin/                    # 后台管理接口
│   │   ├── EmployeeController    # 员工管理
│   │   ├── CategoryController    # 分类管理
│   │   ├── DishController        # 菜品管理
│   │   ├── SetmealController     # 套餐管理
│   │   ├── AdminOrderController  # 订单管理
│   │   └── AdminStatisticsController # 统计分析
│   └── user/                     # 用户端接口
│       ├── UserController        # 用户管理
│       ├── UserDishController    # 菜品浏览
│       ├── ShoppingCartController # 购物车
│       └── UserOrderController   # 订单处理
├── service/                      # 服务层
│   ├── impl/                    # 服务实现类
│   ├── DishService              # 菜品服务
│   ├── OrdersService            # 订单服务
│   ├── UserService              # 用户服务
│   └── StatisticsService        # 统计服务
├── mapper/                       # 数据访问层
├── config/                       # 配置类
├── interceptor/                  # 拦截器
└── task/                        # 定时任务
```

**核心特性**:
- RESTful API设计
- JWT身份认证
- Redis缓存支持
- 分页查询
- 事务管理
- 异常统一处理

### 2. quick-pojo (实体类模块)

**职责**: 定义数据传输对象和实体类

**主要包结构**:
```
com.zmx.quickpojo/
├── entity/                       # 实体类
│   ├── User.java                # 用户实体
│   ├── Employee.java            # 员工实体
│   ├── Category.java            # 分类实体
│   ├── Dish.java                # 菜品实体
│   ├── DishFlavor.java          # 菜品口味实体
│   ├── Setmeal.java             # 套餐实体
│   ├── SetmealDish.java         # 套餐菜品关联实体
│   ├── Orders.java              # 订单实体
│   ├── OrderDetail.java         # 订单明细实体
│   ├── ShoppingCart.java        # 购物车实体
│   └── AddressBook.java         # 地址簿实体
├── dto/                         # 数据传输对象
│   ├── DishFlavorDTO.java       # 菜品口味DTO
│   ├── SetmealAddReqDTO.java    # 套餐添加请求DTO
│   └── SetmealDishDTO.java      # 套餐菜品DTO
├── vo/                          # 视图对象
│   ├── DishPageListRspVO.java   # 菜品分页响应VO
│   ├── SetmealDishVO.java       # 套餐菜品VO
│   └── DishAndSetmealVO.java    # 菜品套餐统一VO
└── handler/                     # 类型处理器
```

**设计特点**:
- 使用Lombok简化代码
- MyBatis-Plus注解支持
- 参数校验注解
- Swagger文档注解
- 字段自动填充

### 3. quick-common (公共模块)

**职责**: 提供通用工具类、配置和基础设施

**主要包结构**:
```
com.zmx.common/
├── annotation/                   # 自定义注解
│   └── ApiLog.java              # API日志注解
├── aspect/                      # AOP切面
│   └── LoggingAspect.java       # 日志切面
├── config/                      # 配置类
│   ├── WebMvcConfig.java        # Web配置
│   ├── SwaggerConfig.java       # Swagger配置
│   ├── RedisConfig.java         # Redis配置
│   ├── MybatisPlusConfig.java   # MyBatis-Plus配置
│   ├── CacheConfig.java         # 缓存配置
│   ├── JwtAdminAuthInterceptor.java # 管理员JWT拦截器
│   └── JwtUserAuthInterceptor.java  # 用户JWT拦截器
├── constants/                   # 常量定义
├── enums/                       # 枚举类
│   └── ErrorCodeEnum.java       # 错误码枚举
├── exception/                   # 异常处理
│   ├── BusinessException.java   # 业务异常
│   └── GlobalExceptionHandler.java # 全局异常处理器
├── properties/                  # 配置属性
├── response/                    # 响应封装
│   ├── Result.java              # 统一响应结果
│   └── PageResult.java          # 分页响应结果
└── utils/                       # 工具类
    ├── JwtUtils.java            # JWT工具类
    ├── BaseContext.java         # 线程上下文工具
    └── AddressUtils.java        # 地址工具类
```

## 核心业务功能

### 后台管理功能
- **员工管理**: 员工账号的增删改查、状态管理
- **分类管理**: 菜品和套餐分类的维护
- **菜品管理**: 菜品信息管理、口味配置、状态控制
- **套餐管理**: 套餐组合管理、价格设置
- **订单管理**: 订单查看、状态更新、统计分析
- **数据统计**: 营业数据统计、图表展示

### 用户端功能
- **用户注册登录**: 微信登录、JWT认证
- **菜品浏览**: 分类浏览、搜索、详情查看
- **购物车**: 添加商品、数量调整、清空购物车
- **订单管理**: 下单、支付、订单跟踪
- **地址管理**: 收货地址的增删改查
- **个人中心**: 个人信息管理、订单历史

## 技术特色

### 1. 分层架构设计
- **Controller层**: 处理HTTP请求，参数校验
- **Service层**: 业务逻辑处理，事务管理
- **Mapper层**: 数据访问，SQL映射
- **Entity层**: 数据模型定义

### 2. 安全认证机制
- JWT Token认证
- 分离的管理员和用户认证
- 拦截器统一处理认证逻辑
- ThreadLocal存储用户上下文

### 3. 缓存策略
- Redis分布式缓存
- Spring Cache注解支持
- 不同业务数据差异化缓存时间
- 缓存穿透保护

### 4. 异常处理
- 全局异常处理器
- 业务异常统一封装
- 参数校验异常处理
- 错误码标准化

### 5. API文档
- Knife4j集成Swagger
- 接口分组管理
- 参数和响应示例
- 在线调试功能

## 数据库设计

### 核心表结构
- **user**: 用户信息表
- **employee**: 员工信息表
- **category**: 分类表
- **dish**: 菜品表
- **dish_flavor**: 菜品口味表
- **setmeal**: 套餐表
- **setmeal_dish**: 套餐菜品关联表
- **orders**: 订单表
- **order_detail**: 订单明细表
- **shopping_cart**: 购物车表
- **address_book**: 地址簿表

## 部署配置

### 环境要求
- JDK 17+
- MySQL 8.0+
- Redis 6.0+
- Maven 3.6+

### 配置文件
主要配置在 `application.yml` 中：
- 数据库连接配置
- Redis连接配置
- JWT密钥配置
- 文件上传配置
- MyBatis-Plus配置

### 启动方式
```bash
# 编译打包
mvn clean package

# 运行应用
java -jar quick-server/target/quick-server-0.0.1-SNAPSHOT.jar
```

## API接口

### 访问地址
- **应用地址**: http://localhost:8081
- **API文档**: http://localhost:8081/doc.html

### 接口分组
- **后台管理接口**: `/admin/**`
- **用户端接口**: `/user/**`

## 开发规范

### 代码规范
- 使用Lombok减少样板代码
- 统一的异常处理机制
- RESTful API设计原则
- 分层架构清晰分离

### 数据库规范
- 统一的字段命名规范
- 自动填充创建时间和更新时间
- 逻辑删除支持
- 主键自动生成策略

### 缓存规范
- 合理的缓存键命名
- 差异化的过期时间设置
- 缓存更新策略
- 避免缓存穿透

## 项目特点

1. **模块化设计**: 清晰的模块划分，便于维护和扩展
2. **现代化技术栈**: 采用Spring Boot 3.0等最新技术
3. **完善的文档**: 详细的API文档和代码注释
4. **高性能**: Redis缓存、连接池优化
5. **安全可靠**: JWT认证、参数校验、异常处理
6. **易于部署**: 标准的Spring Boot应用，支持容器化部署

这个项目展示了现代Java Web应用的最佳实践，适合作为外卖管理系统的参考实现或学习案例。
