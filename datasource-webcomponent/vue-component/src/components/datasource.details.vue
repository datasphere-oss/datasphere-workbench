<template>
  <ValidationObserver ref="form" tag="div" class="flex w-full">
    <collapsable-ui
      :title="dataset.name"
      icon="database"
      :collapse-border="true"
    >
      <div class="inline-flex" slot="left-actions">
        <button
          class="btn btn-xs btn-primary w-8 h-8"
          v-if="!isEditing && !isArchived"
          @click="editDataset()"
          title="编辑数据集"
        >
          <FAIcon icon="pencil-alt"></FAIcon>
        </button>
        
        <div v-if="isEditing">
          <button
            class="btn btn-xs py-1 px-2 btn-primary rounded-0"
            @click="save(updatedDataset)"
            title="保存数据集"
          >
            <FAIcon icon="save"></FAIcon>
          </button>
          <button class="ml-2 text-base" @click="revert(dataset)">
            取消
          </button>
        </div>
      </div>

      <table class="dataset-table">
        <tr>
           <td>
            数据源 
          </td>
          <td>    
          </td>
        </tr>
        <tr>
          <td>
            描述 <span v-if="isEditing" class="text-red-500">*</span>
          </td>
          <td v-if="!isEditing">{{ dataset.datasourceDescription }}</td>
          <td v-if="isEditing">
            <ValidationProvider
              class="flex flex-col"
              name="Description"
              rules="required"
              v-slot="{ errors, classes }"
            >
              <input
                class="form-input w-2/6"
                type="text"
                v-model="updatedDataset.datasourceDescription"
              />
              <span
                class="text-sm text-red-700 flex items-center"
                v-if="errors[0]"
              >
                <FAIcon icon="exclamation-triangle" />
                <span class="ml-1 my-1">{{ errors[0] }}</span>
              </span>
            </ValidationProvider>
          </td>
        </tr>   
       <tr>
          <td>
            数据连接 <span v-if="isEditing" class="text-red-500">*</span>
          </td>
          <td v-if="!isEditing">{{ dataset.category }}</td>
          <td v-if="isEditing">
            <ValidationProvider
              class="flex flex-col"
              name="Read Write Descriptor"
              rules="required"
              v-slot="{ errors, classes }"
            >
              <select  class="form-select w-2/6"  v-model="updatedDataset.category">
                <option value="mongo" disabled>Mongo</option>
                <option value="jdbc">MySQL</option>
                <option value="couch">Couch</option>
              </select>
              <span
                class="text-sm text-red-700 flex items-center"
                v-if="errors[0]"
              >
                <FAIcon icon="exclamation-triangle" />
                <span class="ml-1 my-1">{{ errors[0] }}</span>
              </span>
            </ValidationProvider>
          </td>
        </tr>
        <tr>
          <td>
            读写描述符 <span v-if="isEditing" class="text-red-500">*</span>
          </td>
          <td v-if="!isEditing">{{ dataset.readWriteDescriptor }}</td>
          <td v-if="isEditing">
            <ValidationProvider
              class="flex flex-col"
              name="Read Write Descriptor"
              rules="required"
              v-slot="{ errors, classes }"
            >
              <select  class="form-select w-2/6" v-model="updatedDataset.readWriteDescriptor">
                <option value="all" disabled>所有</option>
                <option value="read">只读 </option>
                <option value="write">只写 </option>
              </select>
              <span
                class="text-sm text-red-700 flex items-center"
                v-if="errors[0]"
              >
                <FAIcon icon="exclamation-triangle" />
                <span class="ml-1 my-1">{{ errors[0] }}</span>
              </span>
            </ValidationProvider>
          </td>
        </tr>

      <tr>
          <td>
            用户名 <span v-if="isEditing" class="text-red-500">*</span>
          </td>
          <td v-if="!isEditing">{{ dataset.dbServerUsername }}</td>
          <td v-if="isEditing">
            <ValidationProvider
              class="flex flex-col"
              name="Database Server User Name"
              rules="required"
              v-slot="{ errors, classes }"
            >
              <input
                class="form-input w-2/6"
                type="text"
                v-model="updatedDataset.dbServerUsername"
              />
              <span
                class="text-sm text-red-700 flex items-center"
                v-if="errors[0]"
              >
                <FAIcon icon="exclamation-triangle" />
                <span class="ml-1 my-1">{{ errors[0] }}</span>
              </span>
            </ValidationProvider>
          </td>
        </tr>
        <tr>
          <td>
            密码 <span v-if="isEditing" class="text-red-500">*</span>
          </td>
          <td v-if="!isEditing">{{ dataset.dbServerPassword }}</td>
          <td v-if="isEditing">
            <ValidationProvider
              class="flex flex-col"
              name="数据库密码"
              rules="required"
              v-slot="{ errors, classes }"
            >
              <input
                class="form-input w-2/6"
                type="text"
                v-model="updatedDataset.dbServerPassword"
              />
              <span
                class="text-sm text-red-700 flex items-center"
                v-if="errors[0]"
              >
                <FAIcon icon="exclamation-triangle" />
                <span class="ml-1 my-1">{{ errors[0] }}</span>
              </span>
            </ValidationProvider>
          </td>
        </tr>
        <tr>
          <td>
            数据库名称 <span v-if="isEditing" class="text-red-500">*</span>
          </td>
          <td v-if="!isEditing">{{ dataset.databaseName }}</td>
          <td v-if="isEditing">
            <ValidationProvider
              class="flex flex-col"
              name="数据库名称"
              rules="required"
              v-slot="{ errors, classes }"
            >
              <input
                class="form-input w-2/6"
                type="text"
                v-model="updatedDataset.databaseName"
              />
              <span
                class="text-sm text-red-700 flex items-center"
                v-if="errors[0]"
              >
                <FAIcon icon="exclamation-triangle" />
                <span class="ml-1 my-1">{{ errors[0] }}</span>
              </span>
            </ValidationProvider>
          </td>
        </tr>
        <tr>
          <td>
            JDBC URL <span v-if="isEditing" class="text-red-500">*</span>
          </td>
          <td v-if="!isEditing">{{ dataset.jdbcUrl }}</td>
          <td v-if="isEditing">
            <ValidationProvider
              class="flex flex-col"
              name="JDBC URL"
              rules="required"
              v-slot="{ errors, classes }"
            >
              <input
                class="form-input w-2/6"
                type="text"
                v-model="updatedDataset.jdbcUrl"
              />
              <span
                class="text-sm text-red-700 flex items-center"
                v-if="errors[0]"
              >
                <FAIcon icon="exclamation-triangle" />
                <span class="ml-1 my-1">{{ errors[0] }}</span>
              </span>
            </ValidationProvider>
          </td>
        </tr>
        <tr>
          <td>
            数据库查询 <span v-if="isEditing" class="text-red-500">*</span>
          </td>
          <td v-if="!isEditing">{{ dataset.dbQuery }}</td>
          <td v-if="isEditing">
            <ValidationProvider
              class="flex flex-col"
              name="数据库查询"
              rules="required"
              v-slot="{ errors, classes }"
            >
              <input
                class="form-input w-2/6"
                type="text"
                v-model="updatedDataset.dbQuery"
              />
              <span
                class="text-sm text-red-700 flex items-center"
                v-if="errors[0]"
              >
                <FAIcon icon="exclamation-triangle" />
                <span class="ml-1 my-1">{{ errors[0] }}</span>
              </span>
            </ValidationProvider>
          </td>
        </tr>
       <tr>
          <td>数据源元数据 </td>
           <td></td>
        </tr>
         <tr>
          <td>
            区域 <span v-if="isEditing" ></span>
          </td>
          <td v-if="!isEditing">{{ dataset.area }}</td>
          <td v-if="isEditing">
            <ValidationProvider
              class="flex flex-col"
              name="区域"  
              v-slot="{ errors, classes }"
            >
              <input
                class="form-input w-2/6"
                type="text"
                v-model="updatedDataset.area"
                disabled
              />
              <span
                class="text-sm text-red-700 flex items-center"
                v-if="errors[0]"
              >
                <FAIcon icon="exclamation-triangle" />
                <span class="ml-1 my-1">{{ errors[0] }}</span>
              </span>
            </ValidationProvider>
          </td>
        </tr>
      <tr>
          <td>
            数据源类型 <span v-if="isEditing" ></span>
          </td>
          <td v-if="!isEditing">{{ dataset.dtype }}</td>
          <td v-if="isEditing">
            <ValidationProvider
              class="flex flex-col"
              name="数据源类型"
              v-slot="{ errors, classes }"
            >
              <input
                class="form-input w-2/6"
                type="text"
                v-model="updatedDataset.dtype"
                disabled
              />
              <span
                class="text-sm text-red-700 flex items-center"
                v-if="errors[0]"
              >
                <FAIcon icon="exclamation-triangle" />
                <span class="ml-1 my-1">{{ errors[0] }}</span>
              </span>
            </ValidationProvider>
          </td>
        </tr>
        <tr>
          <td>
            任务类型 <span v-if="isEditing" ></span>
          </td>
          <td v-if="!isEditing">{{ dataset.ttype }}</td>
          <td v-if="isEditing">
            <ValidationProvider
              class="flex flex-col"
              name="任务类型"
              v-slot="{ errors, classes }"
            >
              <input
                class="form-input w-2/6"
                type="text"
                v-model="updatedDataset.ttype"
                disabled
              />
              <span
                class="text-sm text-red-700 flex items-center"
                v-if="errors[0]"
              >
                <FAIcon icon="exclamation-triangle" />
                <span class="ml-1 my-1">{{ errors[0] }}</span>
              </span>
            </ValidationProvider>
          </td>
        </tr>
          <tr>
          <td>
           属性类型 <span v-if="isEditing" ></span>
          </td>
          <td v-if="!isEditing">{{ dataset.attributetype }}</td>
          <td v-if="isEditing">
            <ValidationProvider
              class="flex flex-col"
              name="属性类型"
              v-slot="{ errors, classes }"
            >
              <input
                class="form-input w-2/6"
                type="text"
                v-model="updatedDataset.attributetype"
                disabled
              />
              <span
                class="text-sm text-red-700 flex items-center"
                v-if="errors[0]"
              >
                <FAIcon icon="exclamation-triangle" />
                <span class="ml-1 my-1">{{ errors[0] }}</span>
              </span>
            </ValidationProvider>
          </td>
        </tr>
          <tr>
          <td>
            Geo-Statistical Type <span v-if="isEditing" ></span>
          </td>
          <td v-if="!isEditing">{{ dataset.gstype }}</td>
          <td v-if="isEditing">
            <ValidationProvider
              class="flex flex-col"
              name="Geo-Statistical Type"
              v-slot="{ errors, classes }"
            >
              <input
                class="form-input w-2/6"
                type="text"
                v-model="updatedDataset.gstype"
                disabled
              />
              <span
                class="text-sm text-red-700 flex items-center"
                v-if="errors[0]"
              >
                <FAIcon icon="exclamation-triangle" />
                <span class="ml-1 my-1">{{ errors[0] }}</span>
              </span>
            </ValidationProvider>
          </td>
        </tr>
          <tr>
          <td>
            格式类型 <span v-if="isEditing" ></span>
          </td>
          <td v-if="!isEditing">{{ dataset.ftype }}</td>
          <td v-if="isEditing">
            <ValidationProvider
              class="flex flex-col"
              name="格式类型"
              v-slot="{ errors, classes }"
            >
              <input
                class="form-input w-2/6"
                type="text"
                v-model="updatedDataset.ftype"
                disabled
              />
              <span
                class="text-sm text-red-700 flex items-center"
                v-if="errors[0]"
              >
                <FAIcon icon="exclamation-triangle" />
                <span class="ml-1 my-1">{{ errors[0] }}</span>
              </span>
            </ValidationProvider>
          </td>
        </tr>  
      </table>
    </collapsable-ui>
  </ValidationObserver>
</template>

<script>

import { mapActions, mapState } from "vuex";
import Dataset from "../store/entities/datasource.entity.js";
import CollapsableUi from "../vue-common/components/ui/collapsable.ui";

export default {
  props: {
    dataset: {
      type: Object
    }
  },
  components: { CollapsableUi },
  data() {
    return {
      updatedDataset: new Dataset(),
      isEditing: false
    };
  },
  computed: {
    ...mapState("app", {
    }),
  },

  watch:{
    dataset() {
      this.updatedDataset = new Dataset(this.dataset);
    }
   },

  created() {
    this.updatedDataset = new Dataset(this.dataset);
  },
  methods: {
    ...mapActions("datasource", ["updateDataset", "getDatasetDetails"]),
    ...mapActions("app", ["showToastMessage"]),
    editDataset() {
      this.isEditing = true;
    },

    async save(updatedDataset) {
      const isValid = await this.$refs.form.validate();
      if (!isValid) {
        return;
      }
      const response = await this.updateDataset(updatedDataset.$toJson());
      if (response.data.status === "Success") {
        await this.getDatasetDetails();
        this.isEditing = false;
        this.showToastMessage({
          id: "global",
          message: `${response.data.message}`,
          type: "success"
        });
      } else {
        this.showToastMessage({
          id: "global",
          message: `${response.data.message}`,
          type: "error"
        });
      }
    },
    revert(dataset) {
      this.updatedDataset = new Dataset(dataset);
      this.isEditing = false;
    }
  }
};
</script>

<style lang="postcss">
table.dataset-table {
  @apply w-full;

  tr {
    td {
      &:first-child {
        @apply text-right font-bold bg-gray-300 text-black;
        @apply border-r border-b border-gray-600;
        @apply w-2/12 p-2;
      }
      &:nth-child(2) {
        @apply text-black;
        @apply border-b border-gray-600;
        @apply pl-2;
      }
    }
  }
}
</style>
