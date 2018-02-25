<template>
	<el-form :model="form" ref="loginForm" label-width="90px" class="login-container">
		<h2>用户注册</h2>
	    <el-form-item label="用户名" required prop="username">
	        <el-input v-model="form.username" placeholder="请输入用户名" ></el-input>
	    </el-form-item>
	    <el-form-item label="密码" prop="password">
	       <el-input v-model="form.password" placeholder="请输入密码"></el-input>
	    </el-form-item>
        <el-form-item label="确认密码" prop="repassword">
	       <el-input v-model="form.repassword" placeholder="请再次输入密码"></el-input>
	    </el-form-item>
	    <el-form-item label="邮箱" prop="email">
	        <el-input v-model="form.email" placeholder="请输入邮箱"></el-input>
	    </el-form-item>
	    <el-form-item>
	        <el-button type="primary" @click="onSubmit">注册</el-button>
	        <el-button>取消</el-button>
	    </el-form-item>
	</el-form>
</template>
<script>
  import {postRequest} from '../utils/api'
  import {putRequest} from '../utils/api'
  
  export default {
  	data(){

		var validatePass = (rule,value,callback) => {
			if(value === ''){
				callback(new Error('请输入密码'));
			} else {
	 		 	if (this.loginForm.checkPass !== value) {
		            this.$refs.loginForm.validateField('repassword');
	         	}
	        }
  			callback();
		};

		var validatePass2 = (rule,value,callback) => {
			if(value===''){
				callback(new Error('请再次输入密码'));
			}else{
				if(this.$refs.loginForm.password!==value){
					callback(new Error('两次输入的密码不一致'));
				}
			}	
		};

  		return {
  			form:{
  				username:'',
  				nickname:'',
  				password:'',
  				repassword:'',
  				email:''
  			},
  			rules:{
  				password:[{
  					validator:validatePass,trigger:'blur',required:true
  				},{
  					min:6,max:9,message:'密码长度在6-9个字符',trigger:'blur'
  				}],
  				repassword:[{
  					validator:validatePass2,trigger:'blur',required:true
  				},{
  					min:6,max:9,message:'密码长度在6-9个字符',trigger:'blur'
  				}],
  				email:[{
  					type:'email',trigger:'blur,change',required:true
  				}],
  				username:[{
  					required:true,message:'请输入用户名',trigger:'blur'
  				}]	
  			}
  		}
  	},
  	methods:{
  		onSubmit:function(){
  			
  		}
  	}
  }
</script>
<style>
.login-container {
    border-radius: 15px;
    background-clip: padding-box;
    margin: 180px auto;
    width: 450px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }
</style>
