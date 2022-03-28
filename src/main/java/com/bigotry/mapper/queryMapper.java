package com.bigotry.mapper;

import com.bigotry.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface queryMapper {
    //查询Id和密码管理员的信息
    public AdminUser queryAdminLoginInfo(@Param("adminName")String adminName,
                                         @Param("password")String password);

    //根据Id和密码查询当前用户信息
    public User queryUserInfo(@Param("userId")String userId,
                                   @Param("password")String password);

    //根据用户ID查询用户
    public User queryUserInfoById(@Param("userId")String userId);

    //用户表更新一条记录
    public Integer updateUser(User user);
    //往用户表新增一条用户记录
    public Integer insertIntoUser(User user);
    //往报刊表新增一条记录
    public Integer insertIntoNewsPaper(NewsPaper paper);
    //查询所有报刊
    public List<NewsPaper> queryALLNewsPaper();

    //查询所有用户
    public List<User> queryAllUser();


    //根据名称查询报刊
    public NewsPaper queryNewsPaperByName(@Param("paperName") String paperName);

    //订阅报刊，往订单表里插入一条数据
    public Integer SubScriptPaper(TbOrder order);

    //查找订单表中用户是否已经订阅该报刊，是则返回True，否则返回False
    public boolean isSubScripted(@Param("userId")String userId,@Param("codeName")String codeName);

    //查询所有订单
    public List<TbOrder> queryAllOrder();

    //根据用户ID查询该用户的所有订单
    public List<TbOrder> queryAllOrderByUserId(@Param("userId")String userId);

    //根据订单编号获取订单对象
    public TbOrder queryOrderByOrderNo(@Param("orderNo")String orderNo);

    //根据订单编号获取报刊对象
    public NewsPaper queryPaperNameByOrderNo(@Param("orderNo")String orderNo);

    //根据订单编号删除一条订单记录
    public Integer deleteOrderByOrderNo(@Param("orderNo")String orderNo);
    //根据报刊代号查询报刊名称
    public String queryNewsPaperByCodeName(@Param("codeName")String codeName);

    //查询所有部门信息
    public List<Department> queryAllDepartment();

    //根据部门号查询该部门下的所有用户
    public List<User> queryAllUserByDeptNo(@Param("deptNo") String deptNo);

    //根据部门号查询部门信息
    public Department queryDeptByDeptNo(@Param("deptNo")String deptNo);
    //根据部门名称查询部门信息
    public Department queryDeptByDeptName(@Param("deptName")String deptName);
    //获取所有有订阅用户的信息
    public List<User> querySubScriptedUser();

    //查询给定用户订阅了哪些报刊
    public List<NewsPaper> querySubScriptedNewsPaperByUserId(@Param("userId")String userId);

    //根据报刊名称查询该报纸的所有订单信息
    public List<TbOrder> queryAllOrderByPaperName(@Param("paperName")String paperName);

    //根据部门号查询该部门下所有用户订阅的报刊的报刊代号
    public List<TbOrder> queryCodeNameByDeptNo(@Param("deptNo")String deptNo);

    //根据部门号查询该部门号是否存在
    public boolean queryDeptNoIsExist(@Param("deptNo") Integer deptNo);
    //根据分类编号查询该分类编号是否存在
    public boolean querySortNoIsExist(@Param("sortNo") Integer sortNo);
    //根据报刊名称删除报刊
    public Integer deletePaperByPaperName(@Param("paperName")String paperName);

    //修改报刊信息
    public Integer updatePaper(NewsPaper paper);

    //根据报刊代号获取报刊对象
    public NewsPaper queryPaperByCodeName(@Param("codeName")String codeName);
}
