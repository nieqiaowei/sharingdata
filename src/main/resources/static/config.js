/**
 * Created by lenovo on 2020/4/27.
 */
(function (){
    var v="v1.0.0";

    var appConfig={
        appName:"数据发布系统",

        appVersion:v,
        //搜索服务key
        city:"BJ",

        //接口地址前缀--dev
        //webURL: 'http://127.0.0.1:8084/sharingdata/',
		
		//打包使用
		webURL: './sharingdata/',
		

        //API地址
        API:{
            //root:"http://124.251.60.205/disaster",

            //登录
            login:"login",
			
			//获取用户公钥
			getpublickey:"getpublickey",
			
			//用户列表
			getuserinfos: "getuserinfos",
            
			//添加或修改用户信息
			saveinfo: "saveinfo",
			
			//添加或修改用户信息
			deleteinfo: "deleteinfo",
			
			//列表
			dataroleinfos:"dataroleinfos",
			
			//添加或修改信息
			saveroleinfo:"saveroleinfo",
			
			//删除信息
			delroleinfo:"delroleinfo",
			
			//
			sourcetypeinfos:"sourcetypeinfos",
			
			//
			savesourcetypeinfo:"savesourcetypeinfo",
			
			//
			delsourcetypeinfoinfo:"delsourcetypeinfoinfo",
			
			//
			getproducerinfos:"getproducerinfos",
			
			//
			datasourceinfos:"datasourceinfos",
			
			//
			savedatasourceinfo:"savedatasourceinfo",
			
			//
			deldatasourceinfo:"deldatasourceinfo",
			
			//
			dataproducerinfos:"dataproducerinfos",
			
			//
			saveproducerinfo:"saveproducerinfo",
			
			//
			delproducerinfo:"delproducerinfo",
			
			//
			datasourcetablesinfo:"datasourcetablesinfo",
			
			//
			datasourcecloumninfo:"datasourcecloumninfo",
			
			//
			datasourcesqlinfo:"datasourcesqlinfo",
			
			//
			datasourceparameterinfo:"datasourceparameterinfo",
			
			//
			sysinfo:"sysinfo",
			
			//
			syssaveinfo:"syssaveinfo",
			
			//
			layerinfo:"layerinfo"			
        }
    }
    window.ZHCJAppConfig=appConfig;
})()