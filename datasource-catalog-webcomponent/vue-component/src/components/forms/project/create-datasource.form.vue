<template>
  <ValidationObserver ref="form" tag="div" class="flex flex-col w-full">
    <ToastUI
      id="dataset-form"
      class="relative p-2"
      innerClass="w-full"
    ></ToastUI>
    <div class="p-3" style="overflow-y: scroll; height: 300px;">
      <div class="flex mb-2">
        <label class="mt-2">数据源
        </label>
        <hr>
      </div>
      <div class="flex mb-2">
        <div class="flex-1 flex flex-col mr-2">
          <label class="mt-2">
            数据源名称:
            <span class="text-red-500">*</span>
          </label>
          <ValidationProvider
            class="flex flex-col"
            name="Dataset name"
            rules="required|startAlpha"
            v-slot="{ errors, classes }"
          >
            <input
              type="text"
              class="form-input"
              :class="classes"
              v-model="updatedDataset.name"
              placeholder="Enter Datasource Name"
            />
            <span
              class="text-sm text-red-700 flex items-center"
              v-if="errors[0]"
            >
              <FAIcon icon="exclamation-triangle" />
              <span class="ml-1 my-1">{{ errors[0] }}</span>
            </span>
          </ValidationProvider>
        </div>
      </div>
      <div class="flex flex-col">
        <label class="mt-2">数据源描述
          <span class="text-red-500">*</span></label>
          <ValidationProvider
              class="flex flex-col"
              name="Dataset Description"
              rules="required"
              v-slot="{ errors, classes }"
            >
        <textarea
          class="form-textarea"
          rows="4"
          maxlength="1000"
          v-model="updatedDataset.datasourceDescription"
          placeholder="Enter Datasource Description"
        ></textarea>
        <span class="leading-none text-right text-gray-600 mt-1"
          >{{ 1000 - updatedDataset.datasourceDescription.length }} 字符</span
        >
        <span class="text-sm text-red-700 flex items-center" v-if="errors[0]">
                <FAIcon icon="exclamation-triangle" />
                <span class="ml-1 my-1">{{ errors[0] }}</span>
              </span>
            </ValidationProvider>
      </div>
       <div class="flex mb-2">
		 <div class="flex-1 flex flex-col mr-2">
            <label class="mt-2">数据连接器
			<span class="text-red-500">*</span></label>
            <ValidationProvider
              class="flex flex-col"
              name="Data Connector"
              rules="required"
              v-slot="{ errors, classes }"
            >
              <select class="form-select" v-model="category">
                <option value>选择</option>
                <option value="mongo" disabled>Mongo</option>
                <option value="mysql">MySQL</option>
                <option value="couch">Couch</option>
              </select>
              <span class="text-sm text-red-700 flex items-center" v-if="errors[0]">
                <FAIcon icon="exclamation-triangle" />
                <span class="ml-1 my-1">{{ errors[0] }}</span>
              </span>
            </ValidationProvider>
          </div> 
        <div class="flex-1 flex flex-col mr-2">
            <label class="mt-2">读写描述符
			<span class="text-red-500">*</span></label>
            <ValidationProvider
              class="flex flex-col"
              name="读写描述符"
              rules="required"
              v-slot="{ errors, classes }"
            >
              <select class="form-select" v-model="readWriteDescriptor">
                <option value>选择</option>
                <option value="all" disabled>所有</option>
                <option value="read">只读</option>
                <option value="write">只写</option>
              </select>
              <span class="text-sm text-red-700 flex items-center" v-if="errors[0]">
                <FAIcon icon="exclamation-triangle" />
                <span class="ml-1 my-1">{{ errors[0] }}</span>
              </span>
            </ValidationProvider>
          </div>
        </div> 

         <div class="flex mb-2">
          <div class="flex-1 flex flex-col mr-2">
            <label class="mt-2">
              用户名
              <span class="text-red-500">*</span>
            </label>
            <ValidationProvider
              class="flex flex-col"
              name="用户名"
              rules="required"
              v-slot="{ errors, classes }"
            >
              <input
                type="text"
                class="form-input"
                v-model="updatedDataset.dbServerUsername"
                placeholder="输入用户名"
              />
              <span class="text-sm text-red-700 flex items-center" v-if="errors[0]">
                <FAIcon icon="exclamation-triangle" />
                <span class="ml-1 my-1">{{ errors[0] }}</span>
              </span>
            </ValidationProvider>
          </div>
          <div class="flex-1 flex flex-col mr-2">
            <label class="mt-2">
              数据库密码
              <span class="text-red-500">*</span>
            </label>
            <ValidationProvider
              class="flex flex-col"
              name="数据库密码"
              rules="required"
              v-slot="{ errors, classes }"
            >
              <input
                type="password"
                class="form-input"
                v-model="updatedDataset.dbServerPassword"
                placeholder="输入密码"
              />
              <span class="text-sm text-red-700 flex items-center" v-if="errors[0]">
                <FAIcon icon="exclamation-triangle" />
                <span class="ml-1 my-1">{{ errors[0] }}</span>
              </span>
            </ValidationProvider>
          </div>
		        <div class="flex-1 flex flex-col mr-2">
            <label class="mt-2">
              数据库名称
              <span class="text-red-500">*</span>
            </label>
            <ValidationProvider
              class="flex flex-col"
              name="数据库名称"
              rules="required"
              v-slot="{ errors, classes }"
            >
              <input
                type="text"
                class="form-input"
                v-model="updatedDataset.databaseName"
                placeholder="输入数据库名称"
              />
              <span class="text-sm text-red-700 flex items-center" v-if="errors[0]">
                <FAIcon icon="exclamation-triangle" />
                <span class="ml-1 my-1">{{ errors[0] }}</span>
              </span>
            </ValidationProvider>
          </div>
        </div> 

      <div class="flex mb-2">
        <div class="flex-1 flex flex-col mr-2">
          <label class="mt-2">
            JDBC URL:
            <span class="text-red-500">*</span>
          </label>
          <ValidationProvider
            class="flex flex-col"
            name="JDBC URL"
            rules="required"
            v-slot="{ errors, classes }"
          >
            <input
              type="text"
              class="form-input"
              :class="classes"
              placeholder="输入 JDBC URL"
              v-model="jdbcURL"
            />
            <span
              class="text-sm text-red-700 flex items-center"
              v-if="errors[0]"
            >
              <FAIcon icon="exclamation-triangle" />
              <span class="ml-1 my-1">{{ errors[0] }}</span>
            </span>
          </ValidationProvider>
        </div>
      </div>
      <div class="flex flex-col">
        <label class="mt-2">数据库查询
         <span class="text-red-500">*</span>
         </label>
          <ValidationProvider
              class="flex flex-col"
              name="数据库查询"
              rules="required"
              v-slot="{ errors, classes }"
            >
        <textarea
          class="form-textarea"
          rows="4"
          maxlength="1000"
          v-model="updatedDataset.dbQuery"
          placeholder="数据库查询"
        ></textarea>
        <span class="leading-none text-right text-gray-600 mt-1"
          >{{ 1000 - updatedDataset.dbQuery.length }} 字符</span
        >
        <span class="text-sm text-red-700 flex items-center" v-if="errors[0]">
                <FAIcon icon="exclamation-triangle" />
                <span class="ml-1 my-1">{{ errors[0] }}</span>
              </span>
            </ValidationProvider>
      </div> 

        <div class="flex mb-2">
        <label class="mt-2">数据源元数据
        </label>
        <hr>
      </div>
      <div class="flex mb-2">
          <div class="flex-1 flex flex-col mr-2">
            <label class="mt-2">区域 </label>
              <input
                type="text"
                class="form-input"
                v-model="area"
                placeholder="输入区域"
				disabled
              />
          </div>
          
           <div class="flex-1 flex flex-col mr-2">
            <label class="mt-2">数据源类型
            <ValidationProvider
              class="flex flex-col"
              name="数据源类型"
              rules=""
              v-slot="{ errors, classes }"
            >
              <select class="form-select" v-model="datasetType">
                <option value>选择</option>
              </select>
              <span class="text-sm text-red-700 flex items-center" v-if="errors[0]">
                <FAIcon icon="exclamation-triangle" />
                <span class="ml-1 my-1">{{ errors[0] }}</span>
              </span>
            </ValidationProvider>
          </div> 
		         <div class="flex-1 flex flex-col mr-2">
            <label class="mt-2">任务类型</label>
              <select class="form-select" v-model="taskType">
                <option value>选择</option>
              </select>
          </div> 
        </div> 

     <div class="flex mb-2">
          <div class="flex-1 flex flex-col mr-2">
            <label class="mt-2">属性类型</label>
              <select class="form-select" v-model="attributeType">
                <option value>选择</option>
              </select>
          </div> 
           <div class="flex-1 flex flex-col mr-2">
            <label class="mt-2">Geo-Statistical Type</label>
              <select class="form-select" v-model="geoType">
                <option value>选择</option>
              </select>
          </div> 
		         <div class="flex-1 flex flex-col mr-2">
            <label class="mt-2">格式类型</label>
              <select class="form-select" v-model="formatType">
                <option value>选择</option>
              </select>
          </div> 
        </div> 

       <div class="flex mb-2">
        <label class="mt-2">自定义元数据</label><hr>
      </div>
        <!--<div class="flex flex-col">
 <vue-good-table
              v-if="!isEmpty"
              :columns="columns"
              :rows="notebooks"
              :line-numbers="true"
              :pagination-options="{ enabled: true, perPage: 5 }"
              :search-options="{ enabled: true, externalQuery: searchTerm }"
              :sort-options="sortOptions"
            >
              <template slot="table-row" slot-scope="props">
                <div class="flex justify-center" v-if="props.column.field === 'key'">
                    <FAIcon
                    class="text-gray-500"
                     icon="minus"
                    v-if="props.row.key=== ''"
                  ></FAIcon>
                </div>
              <div class="flex justify-center" v-if="props.column.field === 'actions'">
                     <button
                  class="btn btn-sm btn-primary text-white mr-2"
                  @click="editNotebook()"
                  :disabled="!loginAsOwner"
                  title="Create Notebook"
                >
                  <FAIcon icon="plus-square" />
                </button>
              </div>
              <div class="flex justify-center" v-if="props.column.field === 'value'">
                    <FAIcon
                    class="text-gray-500"
                     icon="minus"
                    v-if="props.row.value=== ''"
                  ></FAIcon>
                </div>
                 <div
                v-else-if="props.column.field === 'key'"
                  class="break-all justify-center px-1"
                >{{ props.formattedRow[props.column.field] }}
                </div> 
              </template> 
            </vue-good-table> 
          </div>-->
      

		  
    </div>
    <div
      class="flex justify-between py-3 px-2 bg-gray-100 border-gray-200 border-t"
    >
      <button class="btn btn-sm btn-secondary" @click="reset()">重置</button>
      <button class="btn btn-sm btn-primary" @click="save(updatedDataset)">
        {{ isNew ? "创建" : "保存" }} 数据源
      </button>
    </div>
  </ValidationObserver>
</template>

<script>
import { isUndefined } from "lodash-es";
import ToastUI from "../../../vue-common/components/ui/Toast.ui";
import { mapActions } from "vuex";
import Dataset from '../../../store/entities/datasource.entity';

export default {
  components: { ToastUI },
  // props: {
  //   data: {
  //     type: Object
  //   }
  // },
  data() {
    return {
      // columns: [   
      //   {
      //     label: "Actions",
      //     field: "actions"
      //   },
      //     {
      //     label: "Key",
      //     field: "key"
      //   },
      //   {
      //     label: "Value",
      //     field: "value"
      //   },
      // ],
      dataset: new Dataset(),
      category: "",
      readWriteDescriptor: "",
      datasetType: "",
      taskType: "",
      attributeType: "",
      geoType: "",
      formatType: "",
    };
  },
  created() {
   this.updatedDataset =  new Dataset(this.updatedDataset) ;
  },
  computed: {
    isNew() {
      return isUndefined(this.data);
    }
  },
  methods: {
    ...mapActions("datasource", ["createDataset", "allDatasets"]),
    ...mapActions("app", ["showToastMessage"]),
    async save(dataset) {
      const isValid = await this.$refs.form.validate();
      let response = null;
        if (isValid) {
         this.updatedDataset.readWriteDescriptor = this.readWriteDescriptor;
         this.updatedDataset.category = this.category;
         if((this.jdbcURL).split(':')){
            this.updatedDataset.serverName = (this.jdbcURL).substr(0,(this.jdbcURL).lastIndexOf(':'));
            this.updatedDataset.portNumber  = (this.jdbcURL).substr((this.jdbcURL).lastIndexOf(':')+1);
         }
        if (this.isNew) {
          dataset = new Dataset(this.updatedDataset);
          const datasetCreation = dataset.$toJson();
          response = await this.createDataset(datasetCreation);
          if (response.data.status === "Success") {
            await this.allDatasets();
            this.$emit("onSuccess");
            this.reset();
            this.showToastMessage({
              id: "global",
              message: `${response.data.message}`,
              type: "success"
            });
          } else {
            this.showToastMessage({
              id: "dataset-form",
              message: `${"创建数据源时出错，请检查详细信息"}`,
              type: "error"
            });
          }
        }
      }
        
    },
    reset() {
      this.updatedDataset = new Dataset();
      this.category= "";
      this.jdbcURL = "";
      this.readWriteDescriptor="";
      this.$refs.form.reset();
    }
  }
};
</script>
