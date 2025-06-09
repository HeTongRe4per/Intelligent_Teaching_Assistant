import { reactive } from "vue";

const state: { activeKey: string | null; tabHistory: Array<{ index: string; title: any }> } = reactive({
  activeKey: null,
  tabHistory: []
});
const handleAddTab = (pageItem: { index: string; title: any }) => {
  if (state.tabHistory.every((item: any) => item.index !== pageItem.index)) {
    state.tabHistory.push(pageItem);
  }
  handleActiveTab(pageItem.index);
};
const handleActiveTab = (pageIndex: string) => {
  state.activeKey = pageIndex;
};
const handleRemoveTab = (pageIndex: string) => {
  const idx = state.tabHistory.findIndex((item: any) => item.index === pageIndex);
  state.tabHistory.splice(idx, 1);
};

export default {
  state,
  handleAddTab,
  handleActiveTab,
  handleRemoveTab
};
