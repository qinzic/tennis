<template>
  <el-dialog
    :model-value="dialogVisible"
    :title="dialogTitle"
    width="50%"
    @close="handleClose"
  >
    <el-form ref="formRef" :model="form" label-width="100px" :rules="rules">

      <el-form-item label="商品名称" prop="name">
        <el-input v-model="form.name" style="width: 400px"></el-input>
      </el-form-item>

      <el-form-item label="商品价格" prop="price">
        <el-input v-model="form.price" style="width: 100px"></el-input>
      </el-form-item>

      <el-form-item label="商品库存" prop="stock">
        <el-input v-model="form.stock" style="width: 100px"></el-input>
      </el-form-item>

      <el-form-item label="商品类别" >
        <el-select v-model="bigTypeId"  class="m-2" placeholder="请选择商品大类..." @change="handleBigTypeChange">
          <el-option
            v-for="item in bigTypeSlectOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          >
          </el-option>
        </el-select>
        &nbsp;&nbsp;
        <el-select v-model="form.type.id" class="m-2" placeholder="请选择商品小类..." >
          <el-option
            v-for="item in smallTypeSlectOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          >
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="商品描述" prop="description">
        <el-input
          v-model="form.description"
          :rows="4"
          type="textarea"
        />
      </el-form-item>

      <el-form-item label="商品介绍" >
<!--        <el-input
          v-model="form.productIntroImgs"
          :rows="4"
          type="textarea"
        />-->

      </el-form-item>

      <QuillEditor
        v-model:content="form.productIntroImgs"
        contentType="html"
        toolbar="full"
        theme="snow"
        style="height:200px"
      />

      <br/>
      <br/>
      <el-form-item label="商品参数" >
<!--        <el-input
          v-model="form.productParaImgs"
          :rows="4"
          type="textarea"
        />-->
      </el-form-item>

      <QuillEditor
        v-model:content="form.productParaImgs"
        contentType="html"
        toolbar="full"
        theme="snow"
        style="height:200px"
      />


    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleConfirm"
        >确认</el-button
        >
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { defineEmits,ref ,defineProps,watch} from 'vue'
import axios from "@/util/axios";
import { ElMessage } from "element-plus";



const props=defineProps({
  dialogTitle:{
    type:String,
    default:'',
    required:true
  },
  dialogValue:{
    type:Object,
    default:()=>{}
  }
})

const bigTypeId=ref("")

const form=ref({
  id:-1,
  name:'',
  price:0,
  stock:0,
  type:{
    id:''
  },
  remark:'',
  productIntroImgs:'',
  productParaImgs:''
})



const emits=defineEmits(['update:modelValue','initProductList'])

const formRef=ref(null)


const handleClose = () => {
  formRef.value.resetFields();
  emits('update:modelValue',false)
}



const rules=ref({
  name:[
    {
      required: true,
      message: '请输入商品小类名称！',
    }
  ],
  price:[
    { required: true, message: '请输入商品价格!' },
    { type: 'number', message: '商品价格是数值类型！',transform: (value) => Number(value) },
  ],
  stock:[
    { required: true, message: '请输入商品库存!' },
    { type: 'number', message: '商品库存是数值类型！',transform: (value) => Number(value) },
  ],
  description:[
    {
      required: true,
      message: '请输入商品小类描述！',
    },
  ],
  productIntroImgs:[
    {
      required: true,
      message: '请输入商品介绍！',
    }
  ],
  productParaImgs:[
    {
      required: true,
      message: '请输入商品参数！',
    }
  ]
})

const bigTypeSlectOptions =ref([])

const smallTypeSlectOptions =ref([])


const initBigTypeSelectList=async()=>{
  console.log('xxx')
  const res=await axios.post("admin/bigType/listAll");
  bigTypeSlectOptions.value=res.data.bigTypeList;
}

const initSmallTypeSelectList=async(bigTypeId)=>{
  console.log('bigTypeId='+bigTypeId)
  const res=await axios.post("admin/smallType/listAll/"+bigTypeId);
  smallTypeSlectOptions.value=res.data.smallTypeList;
}


initBigTypeSelectList();

const handleConfirm = () => {

  if(form.value.type.id==""){
    ElMessage.error("请选择商品类别");
    return;
  }
  formRef.value.validate(async (valid) => {
    if (valid) {
      console.log("xxx")
       try{
        let result=await axios.post("admin/product/save",form.value);
        let data=result.data;
        if(data.code==0){
          ElMessage.success("执行成功");
          formRef.value.resetFields();
          bigTypeId.value=""
          emits("initProductList")
          handleClose();
        }else{
          ElMessage.error(data.msg);
        }
      }catch(err){
        console.log("error:"+err)
        ElMessage.error('系统运行出错，请联系管理员');
      }
    } else {
      return false
    }
  })
}

const handleBigTypeChange = (val) => {
  console.log("val="+val)
  initSmallTypeSelectList(val);
}

const getBigTypeIdBySmallTypeId=async (smallTypeId)=>{
  console.log('xxx')
  const res=await axios.get("admin/smallType/getBigTypeIdBySmallTypeId/"+smallTypeId);
  console.log(res.data.bigTypeId)
  handleBigTypeChange(res.data.bigTypeId)
  //return res.data.bigTypeId;
}


watch(
  ()=>props.dialogValue,
  ()=>{
    console.log("dialogValue222="+props.dialogValue);
    form.value=props.dialogValue;
    console.log(form.value.id)
    console.log(form.value.productParaImgs)
    if(form.value.id==undefined){
      console.log("是添加")
    }else{
      console.log("是修改")
      //bigTypeId.value=2;
      getBigTypeIdBySmallTypeId(form.value.type.id);
    }
  },
  {deep:true,immediate:true}
)

</script>

<style scoped>

</style>