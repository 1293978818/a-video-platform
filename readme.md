
# work4 - 102300230

## 1.项目结构

``
work4
├─ pom.xml //项目依赖
├─ src
   ├─ main
   │  ├─ java
   │  │  └─ com
   │  │     └─ ygh
   │  │        ├─ config
   │  │        │  ├─ MpConfig.java //mybatis-plus配置
   │  │        │  └─ WebSecurityConfig.java //security配置
   │  │        ├─ controller
   │  │        │  ├─ CommentController.java //评论控制器
   │  │        │  ├─ ExceptionAdvice.java //异常处理
   │  │        │  ├─ FollowController.java //关注控制器
   │  │        │  ├─ LikeController.java //点赞控制器
   │  │        │  ├─ UserController.java //用户控制器
   │  │        │  └─ VideoController.java //视频控制器
   │  │        ├─ domain
   │  │        │  ├─ Base.java //响应基类
   │  │        │  ├─ Comment.java //评论实体类
   │  │        │  ├─ Follow.java //关注实体类
   │  │        │  ├─ Like.java //点赞实体类
   │  │        │  ├─ MyUserDetails.java //用户实体类
   │  │        │  ├─ Result.java //响应实体类
   │  │        │  ├─ User.java //用户实体类
   │  │        │  ├─ Users.java //用户实体类
   │  │        │  ├─ Video.java //视频实体类
   │  │        │  └─ Videos.java //视频实体类
   │  │        ├─ exception
   │  │        │  └─ BizException.java //业务异常
   │  │        ├─ filter
   │  │        │  └─ TokenCheckFilter.java //token校验
   │  │        ├─ handler
   │  │        │  ├─ MyAuthenticationFailureHandler.java //登录失败处理
   │  │        │  ├─ MyAuthenticationSuccessHandler.java //登录成功处理
   │  │        │  └─ MyMetaObjectHandler.java //mybatis-plus自动填充
   │  │        ├─ mapper
   │  │        │  ├─ CommentMapper.java //评论mapper
   │  │        │  ├─ FollowMapper.java //关注mapper
   │  │        │  ├─ LikeMapper.java //点赞mapper
   │  │        │  ├─ UserMapper.java //用户mapper
   │  │        │  └─ VideoMapper.java //视频mapper
   │  │        ├─ service
   │  │        │  ├─ CommentService.java //评论服务
   │  │        │  ├─ FollowService.java //关注服务
   │  │        │  ├─ impl
   │  │        │  │  ├─ CommentServiceImpl.java //评论服务实现
   │  │        │  │  ├─ FollowServiceImpl.java //关注服务实现
   │  │        │  │  ├─ LikeServiceImpl.java //点赞服务实现
   │  │        │  │  ├─ UserDetailsServiceImpl.java //用户服务实现
   │  │        │  │  ├─ UserServiceImpl.java //用户服务实现
   │  │        │  │  └─ VideoServiceImpl.java //视频服务实现
   │  │        │  ├─ LikeService.java //点赞服务
   │  │        │  ├─ UserService.java //用户服务
   │  │        │  └─ VideoService.java //视频服务
   │  │        ├─ util
   │  │        │  └─ JwtUtil.java //jwt工具
   │  │        └─ Work4Application.java //启动类
   │  └─ resources
   │     ├─ application.yml //配置文件
   │     └─ META-INF
   │        └─ additional-spring-configuration-metadata.json
   └─ test //测试类略
      └─ java
         └─ com
            └─ ygh
               └─ Work4ApplicationTests.java //启动测试类
``

## 2.功能介绍

### 2.1 用户模块

- 用户注册
- 用户登录
- 查询用户信息
- 上传头像

### 2.2 视频模块

- 视频上传
- 视频列表
- 视频搜索
- 热门排行榜

### 2.3 互动模块

- 点赞操作
- 点赞列表
- 评论操作
- 评论列表
- 删除评论

### 2.4 社交模块

- 关注操作
- 关注列表
- 粉丝列表
- 朋友列表

## 3. 技术栈

- SpringBoot
- Mybatis-Plus
- MySQL
- Redis
- JWT
- SpringSecurity

## 4. 项目不足

- 部分业务未实现联表查询
