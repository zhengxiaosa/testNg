//package bank;
//
//
//import com.google.gson.JsonObject;
//import com.kuaishou.ep.dao.budget.BudgetDimAppsMapper;
//import com.kuaishou.ep.dao.budget.VersionBaseMapper;
//import com.kuaishou.ep.dao.budget.VersionOptLogMapper;
//import com.kuaishou.ep.eaUtils.DataProviderUtil;
//import com.kuaishou.ep.eaUtils.GlobalApi;
//import com.kuaishou.ep.eaUtils.HttpUtil;
//import com.kuaishou.ep.eaUtils.TestCaseBases;
//import com.kuaishou.ep.khaosCommon.common.ClassAnnotation;
//import com.kuaishou.ep.khaosCommon.entity.HttpResponseData;
//import com.kuaishou.ep.khaosCommon.utils.GsonUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.testng.annotations.AfterClass;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.DataProvider;
//import org.testng.annotations.Test;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@ClassAnnotation(
//        author = "wb_wubianli",
//        description = "使用clone方式创建预算app版本，变更预算有效版本、添加预算明细、删除预算明细、查看预算日志",
//        createTime = "2021/8/19"
//)
//
//@Slf4j
//public class CloneBudgetAppTest extends TestCaseBases {
//
//    @Autowired
//    GlobalApi globalApi;
//    @Autowired
//    VersionBaseMapper versionBaseMapper;
//    @Autowired
//    BudgetDimAppsMapper budgetDimAppsMapper;
//    @Autowired
//    VersionOptLogMapper versionOptLogMapper;
//
//    private String versionType;
//    private String dimID;
//    private String budgetVersion;
//    private String cloneVersion;
//    private String createVersion;
//    private HashMap<String, String> header;
//
//    @BeforeClass
//    public void init() {
//
//        header = new HashMap<>();
//        header.put("Cookie", globalApi.Cookie);
//    }
//
//    @DataProvider
//    public Object[][] getTestData() {
//        return DataProviderUtil.getTestData("test/data/BudgetSceneData/cloneBudgetAppData.json");
//    }
//
//    @Test(dataProvider = "getTestData", description = "创建预算类型")
//    public void createBudgetVersionTest(String comments, JsonObject request, JsonObject extResult) throws Exception {
//
//        log.info("请求用例用例名称：", comments);
//        String createBudgetVersionUrl = GlobalApi.testIp + GlobalApi.createBudgetVersionUrl;
//        Map param = GsonUtil.jsonStrToBean(request.get("createBudgetVersion").toString(), Map.class);
//        //创建版本
//        HttpResponseData httpResult = HttpUtil.doPost(createBudgetVersionUrl, param, header, null);
//        JsonObject result = GsonUtil.strToJsonObject(httpResult.getBody());
//        DataProviderUtil.AssertJson(extResult.getAsJsonObject("createBudgetVersion"), httpResult.getBody());
//        //获取创建后的版本号
//        if (result.get("code").toString().equals("0")&result.get("message").toString().equals("\"OK\"")){
//            createVersion = result.get("result").toString();
//            createVersion = createVersion.substring(1,createVersion.length()-1);
//        }
//
//    }
//
//    @Test(dataProvider = "getTestData",dependsOnMethods ="createBudgetVersionTest" ,description = "使用clone功能创建预算版本")
//    public void cloneBudgetVersionTest(String comments, JsonObject request, JsonObject extResult) throws Exception {
//
//        log.info("请求用例用例名称{}", comments);
//        String cloneBudgetVersionUrl = globalApi.testIp + globalApi.cloneBudgetVersion;
//        //查询数据获取当前预算类型的有效版本
//
////        versionType = request.getAsJsonObject("cloneBudgetVersion").get("budgetType").toString();
//        Map versionTypes = GsonUtil.jsonStrToBean(request.get("cloneBudgetVersion").toString(),Map.class);
//        versionType = versionTypes.get("budgetType").toString();
//        budgetVersion = versionBaseMapper.selectByStatus("Y", versionType).getVersionNum();
//        log.info("通过数据库查询当前预算类型的有效版本为："+budgetVersion);
//        if (budgetVersion != null & budgetVersion.length() > 0) {
//            //更新json文件中的版本号
//            request.getAsJsonObject("cloneBudgetVersion").addProperty("versionNum", budgetVersion);
//            Map param = GsonUtil.jsonStrToBean(request.get("cloneBudgetVersion").toString(), Map.class);
//            HttpResponseData httpResult = HttpUtil.doPost(cloneBudgetVersionUrl, param, header, null);
//            JsonObject result = GsonUtil.strToJsonObject(httpResult.getBody());
//            //获取clone后的版本号
//            if(result.get("code").toString().equals("0") && result.get("message").toString().equals("\"OK\"")) {
//                cloneVersion = result.get("result").toString();
//                cloneVersion = cloneVersion.substring(1,cloneVersion.length()-1);
//                log.info("获取clone后的预算版本号：" + cloneVersion);}
//            DataProviderUtil.AssertJson(extResult.getAsJsonObject("cloneBudgetVersion"), httpResult.getBody());
//        } else {
//            log.info("当前无可用的预算版本");
//        }
//
//
//    }
//
//    @Test(dataProvider = "getTestData", dependsOnMethods = "cloneBudgetVersionTest", description = "将clone的版本设置为有效的版本")
//    public void updateBudgetVersionTest(String comments, JsonObject request, JsonObject extResult) throws Exception {
//
//        log.info("请求用例用例名称{}", comments);
//        String updateBudgetVersionUrl = globalApi.testIp + globalApi.updateBudgetVersion;
//        request.getAsJsonObject("updateBudgetVersion").addProperty("versionNum", cloneVersion);
//        Map param = GsonUtil.jsonStrToBean(request.get("updateBudgetVersion").toString(), Map.class);
//        HttpResponseData httpResponseData = HttpUtil.doPost(updateBudgetVersionUrl, param, header, null);
//        JsonObject result = GsonUtil.strToJsonObject(httpResponseData.getBody());
//        //获取新版本的ID作为全局版本ID
//        DataProviderUtil.AssertJson(extResult.getAsJsonObject("updateBudgetVersion"), httpResponseData.getBody());
//
//    }
//
//    @Test(dataProvider = "getTestData", dependsOnMethods = "updateBudgetVersionTest", description = "查询当前版本是否为有效版本")
//    public void queryBudgetVersionTest(String comments, JsonObject request, JsonObject extResult) throws Exception {
//
//        log.info("请求用例用例名称{}", comments);
//        String queryBudgetVersionUrl = globalApi.testIp + globalApi.queryBudgetVersion;
//        //取请求参数
//        request.getAsJsonObject("queryBudgetVersion").addProperty("versionNum", cloneVersion);
//        Map param = GsonUtil.jsonStrToBean(request.get("queryBudgetVersion").toString(), Map.class);
//        String newbudgetVersion = versionBaseMapper.selectByStatus("Y", versionType).getVersionNum();
//        extResult.getAsJsonObject("queryBudgetVersion").getAsJsonObject("result").addProperty("version",newbudgetVersion);
//        HttpResponseData httpResponseData = HttpUtil.doGet(queryBudgetVersionUrl, param, header, null);
//        DataProviderUtil.AssertJson(extResult.getAsJsonObject("queryBudgetVersion"), httpResponseData.getBody());
//    }
//
//
//    @Test(dataProvider = "getTestData", dependsOnMethods = "queryBudgetVersionTest", description = "新增预算维度")
//    public void addBudgetTest(String comments, JsonObject request, JsonObject extResult) throws Exception {
//
//        log.info("请求用例用例名称{}", comments);
//        String addBudgetUrl = globalApi.testIp + globalApi.addBudget;
//        //取请求参数
//        request.getAsJsonObject("addBudget").addProperty("version", cloneVersion);
//        HttpResponseData httpResult = HttpUtil.doPostJson(addBudgetUrl, request.get("addBudget").toString(), header, null);
////        DataProviderUtil.AssertJson(extResult.getAsJsonObject("addBudget"), httpResult.getBody());
//
//    }
//
//    @Test(dataProvider = "getTestData", dependsOnMethods = "addBudgetTest", description = "检索新添加的预算维度")
//    public void queryBudgetDimTest(String comments, JsonObject request, JsonObject extResult) throws Exception {
//
//        log.info("请求用例用例名称{}", comments);
//        String queryBudgetDimUrl = globalApi.testIp + globalApi.queryBudgetDim;
//        //取请求参数
//        request.getAsJsonObject("queryBudgetDim").addProperty("versionNum", cloneVersion);
//        Map param = GsonUtil.jsonStrToBean(request.get("queryBudgetDim").toString(), Map.class);
//        HttpResponseData httpResponseData = HttpUtil.doGet(queryBudgetDimUrl, param, header, null);
//        //变更extResult的
//        DataProviderUtil.AssertJson(extResult.getAsJsonObject("queryBudgetDim"), httpResponseData.getBody());
//        //获取维度ID
//        JsonObject result = GsonUtil.strToJsonObject(httpResponseData.getBody());
//        if (result.get("code").toString().equals("0") & result.get("message").toString().equals("\"OK\"")) {
//            JsonObject JsonObject = GsonUtil.strToJsonObject(result.getAsJsonObject("result").getAsJsonArray("list").get(0).toString());
//            dimID = JsonObject.get("id").toString();
//        }
//    }
//
//
//    @Test(dataProvider = "getTestData", dependsOnMethods = "queryBudgetDimTest", description = "删除新添加的预算维度")
//    public void deleteBudgetDimTest(String comments, JsonObject request, JsonObject extResult) throws Exception {
//
//        log.info("请求用例用例名称{}", comments);
//        String deleteBudgetDimTestUrl = globalApi.testIp + globalApi.deleteBudgetDim;
//        //取请求参数
//        request.getAsJsonObject("deleteBudgetDim").addProperty("id", dimID);
//        Map param = GsonUtil.jsonStrToBean(request.get("deleteBudgetDim").toString(), Map.class);
//        HttpResponseData httpResponseData = HttpUtil.doPost(deleteBudgetDimTestUrl, param, header, null);
////        DataProviderUtil.AssertJson(extResult.getAsJsonObject("deleteBudgetDim"), httpResponseData.getBody());
//    }
//
//
//    @Test(dataProvider = "getTestData", dependsOnMethods = "deleteBudgetDimTest", description = "查询当前版本的日志")
//    public void budgetVersionLogsTest(String comments, JsonObject request, JsonObject extResult) throws Exception {
//        log.info("请求用例用例名称{}", comments);
//        String budgetVersionLogsUrl = globalApi.testIp + globalApi.budgetVersionLogs;
//        //取请求参数
//        request.getAsJsonObject("budgetVersionLogs").addProperty("versionNum", cloneVersion);
//        Map param = GsonUtil.jsonStrToBean(request.get("budgetVersionLogs").toString(), Map.class);
//        HttpResponseData httpResult = HttpUtil.doGet(budgetVersionLogsUrl, param, header, null);
//        DataProviderUtil.AssertJson(extResult.getAsJsonObject("budgetVersionLogs"), httpResult.getBody());
//    }
//
//    @Test(dataProvider = "getTestData", dependsOnMethods = "budgetVersionLogsTest", description = "还原版本数据")
//    public void changeBudgetVersionTest(String comments, JsonObject request, JsonObject extResult) throws Exception {
//
//        log.info("请求用例用例名称{}", comments);
//        String changeBudgetVersionUrl = globalApi.testIp + globalApi.changeBudgetVersion;
//        request.getAsJsonObject("changeBudgetVersion").addProperty("versionNum", budgetVersion);
//        Map param = GsonUtil.jsonStrToBean(request.get("changeBudgetVersion").toString(), Map.class);
//        HttpResponseData httpResponseData = HttpUtil.doPost(changeBudgetVersionUrl, param, header, null);
//        DataProviderUtil.AssertJson(extResult.getAsJsonObject("changeBudgetVersion"), httpResponseData.getBody());
//
//    }
//
//
//
//    @AfterClass
//    public void clean()  {
//        String versionLast = versionBaseMapper.selectByStatus("Y", versionType).getVersionNum();
//        log.info("当前有效的版本号为{}",versionLast);
//        if (versionLast.length()>0&!versionLast.equals(budgetVersion)){
//            versionBaseMapper.updateByVersion(0,"Y",budgetVersion);
//        }else {
//
//        }
//        budgetDimAppsMapper.deleteByVersion(cloneVersion);
//        versionBaseMapper.deleteByversionNum(cloneVersion);
//        versionBaseMapper.deleteByversionNum(createVersion);
//        versionOptLogMapper.deleteByVersionNum(cloneVersion);
//        versionOptLogMapper.deleteByVersionNum(createVersion);
//
//    }
//
//
//
//}
