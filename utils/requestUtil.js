// 定义公共的url
const baseUrl="http://localhost/";

/**
 * 返回baseUrl
 */
export const getBaseUrl=()=>{
  return baseUrl;
}

/**
 * wx getUserProfile封装
 * @param {*} param0 
 */
export const getUserProfile=()=>{
  return new Promise((resolve,reject)=>{
    wx.getUserProfile({
      desc: '获取用户信息',
      success: (res)=>{
        resolve(res);
      },
      fail:(err)=>{
        reject(err);
      }
    })
  })
}

/**
 * wx login封装
 * @param {*} param0 
 */
export const getLogin=()=>{
  return new Promise((resolve,reject)=>{
    // 获取小程序登录成功后的code
    wx.login({
      timeout: 5000,
      success:(res)=>{
        resolve(res);
      },
      fail:(err)=>{
        reject(err);
      }
    })
  })
}

/**
 * promise形式的 小程序的微信支付
 * @param {*} pay 
 */
export const requestPay=(pay)=>{
  return new Promise((resolve,reject)=>{
    wx.requestPayment({
      ...pay,
      success:(res)=>{
        resolve(res);
      },
      fail:(err)=>{
        reject(err);
      }
    })
  })
}

/**
 * 后端请求工具类
 * @param {*} params 请求参数
 */
export const requestUtil=(params)=>{
  // 处理私有路径的token
  let header={...params.header};
  if(params.url.includes("/my/")){
    header["token"]=wx.getStorageSync("token");
  }

  return new Promise((resolve,reject)=>{
    wx.request({
      ...params,
      header:header,
      url:baseUrl+params.url,
      success:(result)=>{
        resolve(result.data);
      },
      fail:(err)=>{
        reject(err);
      }
    });
  })
}