// 列表项 元数据类型
export interface RecordMateType {
  name: string;
  subName?: string;
  isSubField?: boolean;
  title: string;
  dataType: string;
  options?: any[];
  selectable?: boolean;
  disabled?: boolean;
  required?: boolean;
}
