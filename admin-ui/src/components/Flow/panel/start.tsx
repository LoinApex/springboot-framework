import React from "react";
import {Button, Drawer, Space, Tabs} from "antd";
import EdgePanel from "@/components/Flow/panel/EdgePanel";
import NodePanel from "@/components/Flow/panel/NodePanel";
import {ProForm} from "@ant-design/pro-components";

interface SettingPanelProps {
    visible: boolean;
    setVisible: (visible: boolean) => void;
    properties: any;
    onSettingChange: (values: any) => void;
}

const StartSettingPanel: React.FC<SettingPanelProps> = (props) => {

    const [form] = ProForm.useForm();

    return (
        <Drawer
            title={"节点设置"}
            width={"40%"}
            onClose={() => {
                props.setVisible(false);
            }}
            open={props.visible}
            destroyOnClose={true}
            extra={(
                <Space>
                    <Button
                        type={"primary"}
                        onClick={() => {
                            form.submit();
                            props.setVisible(false);
                        }}
                    >确认</Button>

                    <Button
                        onClick={() => {
                            props.setVisible(false);
                        }}
                    >关闭</Button>
                </Space>
            )}

        >
            <Tabs
                items={[
                    {
                        label: "节点设置",
                        key: "nodes",
                        children: (
                            <NodePanel
                                type={"start"}
                                form={form}
                                id={props.properties?.id}
                                data={props.properties}
                                onFinish={props.onSettingChange}
                            />
                        )
                    },
                    {
                        label: "关系设置",
                        key: "edges",
                        children: (
                            <EdgePanel
                                type={"start"}
                                id={props.properties?.id}/>
                        )
                    }
                ]}
            />

        </Drawer>
    )

}

export default StartSettingPanel;