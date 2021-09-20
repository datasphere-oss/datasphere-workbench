<template>
  <div
    class="flex items-center justify-between my-2 mr-2"
    v-if="total > itemsPerPage"
  >
    <span class="text-gray-700 ml-2"
      >显示 {{ currentPage }} of
      {{ totalPages | pluralize("page", { includeNumber: true }) }}</span
    >
    <div>
      <button
        @click="goToPage(1)"
        :disabled="currentPage === 1"
        class="btn btn-sm btn-secondary rounded-none text-black mx-1"
      >
        首页
      </button>
      <button
        @click="goToPage(currentPage - 1)"
        :disabled="currentPage === 1"
        class="btn btn-sm btn-secondary rounded-none text-black mx-1"
      >
        上一页
      </button>
      <button
        :disabled="currentPage === totalPages"
        @click="goToPage(currentPage + 1)"
        class="btn btn-sm btn-secondary rounded-none text-black mx-1"
      >
        下一页
      </button>
      <button
        @click="goToPage(totalPages)"
        :disabled="currentPage === totalPages"
        class="btn btn-sm btn-secondary rounded-none text-black ml-1"
      >
        尾页
      </button>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    pageChanged: {
      type: Function
    },
    total: {
      type: Number
    },
    itemsPerPage: {
      type: Number
    }
  },
  data() {
    return {
      currentPage: 1
    };
  },
  computed: {
    totalPages() {
      return this.total < this.itemsPerPage
        ? 1
        : Math.ceil(this.total / this.itemsPerPage);
    }
  },
  methods: {
    goToPage(page) {
      this.currentPage = page;
      this.pageChanged({ currentPage: page - 1 });
    }
  }
};
</script>
