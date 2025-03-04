import axios from "axios";

// 0:邮箱登录,1:电话号码登录,2:飞书登录
export async function login(loginType:number,email: string, code: string) {
    const response = await axios.post(
        '/api/users/users/login',
        {"loginType":loginType,"email":email, "code":code},
        {headers:{
                    "Content-Type":"application/json",
                    "Accept": "application/json" }}
    )
    console.log('response created successfully',response.data)
    return response.data
}

//发送邮箱验证码
export async function sendEmailCode(email: string) {
     return await axios.get(
        '/api/users/users/sendEmailCode',
        {params:{"email":email}}
    ).then(response => {
        return response.data
    }).catch(error => {
        console.log(error)
    })
}

//飞书登录
export async function feishuLogin() {
    return await axios.get(
        '/api/users/oauth/render',
    ).then(response => {
        return response.data
    }).catch(error => {
        console.log(error)
    })
}


