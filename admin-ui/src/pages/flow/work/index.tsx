import React from "react";
import Flow, {FlowActionType} from "@/components/Flow";
import {ActionType, ModalForm, PageContainer, ProForm, ProFormText, ProTable} from "@ant-design/pro-components";
import {list, save, schema} from "@/api/flow";
import {Button, Drawer, message, Popconfirm, Space} from "antd";

const FlowPage = () => {

    const [visible, setVisible] = React.useState(false);
    const [editorVisible, setEditorVisible] = React.useState(false);
    const flowActionType = React.useRef<FlowActionType>(null);
    const [form] = ProForm.useForm();
    const actionRef = React.useRef<ActionType>();

    const [current,setCurrent] = React.useState<any>(null);


    const handlerSave = async (values:any)=>{
        const res = await save(values);
        setEditorVisible(false);
        if(res.success){
            message.success("保存成功");
        }
        actionRef.current?.reload();
    }

    const handlerSchema = async (json:any)=>{
        const res = await schema({
            id:current.id,
            schema:json
        });
        setVisible(false);
        if(res.success){
            message.success("保存成功");
        }
        actionRef.current?.reload();
    }

    const columns = [
        {
            title: '编号',
            dataIndex: 'id',
            search: false,
        },
        {
            title: '标题',
            dataIndex: 'title',
        },
        {
            title: '说明',
            dataIndex: 'description',
            valueType: 'text',
            search: false,
        },
        {
            title: '节点数量',
            dataIndex: 'nodes',
            valueType: 'text',
            search: false,
            render: (text: any, record: any) => {
                if(record.nodes) {
                    return record.nodes.length;
                }
                return 0;
            }
        },
        {
            title: '操作',
            valueType: 'option',
            render: (_: any, record: any) => [
                <a
                    key="editable"
                    onClick={() => {
                        form.setFieldsValue(record);
                        setEditorVisible(true);
                    }}
                >
                    编辑
                </a>,
                <a
                    key="design"
                    onClick={() => {
                        setCurrent(record);
                        setVisible(true);
                    }}
                >
                    设计
                </a>,
                <Popconfirm
                    key="delete"
                    title={"确认删除?"}
                    onConfirm={() => {
                        console.log(record);
                    }}
                >
                    <a
                        key="delete"
                    >
                        删除
                    </a>
                </Popconfirm>
            ]
        }

    ] as any[];
    return (
        <PageContainer>
            <ProTable
                actionRef={actionRef}
                rowKey={"id"}
                columns={columns}
                toolBarRender={() => {
                    return [
                        <Button
                            type={"primary"}
                            onClick={() => {
                                form.resetFields();
                                setEditorVisible(true);
                            }}
                        >新增</Button>
                    ]
                }}
                request={async (params, sort, filter) => {
                    return list(params, sort, filter, [
                        {
                            key: "title",
                            type: "LIKE"
                        }
                    ]);
                }}
            />

            <ModalForm
                title="编辑流程"
                form={form}
                open={editorVisible}
                modalProps={{
                    destroyOnClose: true,
                    onClose: () => setEditorVisible(false),
                    onCancel: () => setEditorVisible(false)
                }}
                onFinish={handlerSave}
            >
                <ProFormText
                    name={"id"}
                    hidden={true}
                />

                <ProFormText
                    name={"title"}
                    label={"标题"}
                    rules={[
                        {
                            required: true,
                            message: "请输入标题"
                        }
                    ]}
                />

                <ProFormText
                    name={"description"}
                    label={"描述"}
                />

            </ModalForm>


            <Drawer
                title="流程设计"
                width={"100%"}
                open={visible}
                onClose={() => {
                    setVisible(false);
                }}
                destroyOnClose={true}
                style={{
                    padding: 0,
                    margin: 0
                }}
                extra={
                    <Space>

                        <Button
                            type={"primary"}
                            onClick={async () => {
                                const data = flowActionType.current?.getData();
                                const json = JSON.stringify(data);
                                console.log(json);
                                await handlerSchema(json);
                            }}
                        >
                            保存
                        </Button>

                        <Button
                            onClick={() => {
                                setVisible(false);
                            }}
                        >
                            取消
                        </Button>
                    </Space>
                }
            >
                <Flow
                    data={current?.schema?JSON.parse(current?.schema):null}
                    actionRef={flowActionType}
                />
            </Drawer>

        </PageContainer>
    )
};

export default FlowPage;
