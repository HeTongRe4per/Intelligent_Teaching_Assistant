<template>
  <div>
    <div class="ai-chat-page">
      <el-page-header
        title="AI选课助手"
        style="margin-bottom: 20px"
        @back="() => router.push('/index')"
      ></el-page-header>
      <div class="ai-chat-container">
        <div class="chat-messages" ref="messagesContainer">
          <div v-for="(message, index) in chatMessages" :key="index" :class="['message', message.role]">
            <div class="message-content">
              <div class="message-avatar">
                <el-avatar v-if="message.role === 'assistant'" :size="32">
                  <el-icon><Aim /></el-icon>
                </el-avatar>
                <el-avatar v-else :size="32" style="background-color: #87d068">
                  <el-icon><User /></el-icon>
                </el-avatar>
              </div>
              <div class="message-text" v-html="formatMessage(message.content)"></div>
            </div>
          </div>
          <div v-if="loading" class="message assistant">
            <div class="message-content">
              <div class="message-avatar">
                <el-avatar :size="32">
                  <el-icon><Aim /></el-icon>
                </el-avatar>
              </div>
              <div class="message-text">
                加载中...
              </div>
            </div>
          </div>
        </div>
        <div class="chat-input">
          <el-input
            type="textarea"
            v-model="userInput"
            placeholder="请输入您的问题..."
            :auto-size="{ minRows: 3, maxRows: 5 }"
            @keyup="onKeyUp"
          />
          <div class="input-actions">
            <el-button type="primary" @click="handleSendMessage" :loading="loading">发送</el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { nextTick, onMounted, ref } from "vue";
import axios from "axios";
import { ElMessage } from "element-plus";
import router from "@/router";
import { Aim, User } from "@element-plus/icons-vue";

const userInput = ref("");
const chatMessages = ref<Array<{ role: string; content: string }>>([]);
const loading = ref(false);
const messagesContainer = ref<HTMLElement | null>(null);

onMounted(() => {
  // 添加欢迎消息
  chatMessages.value.push({
    role: "assistant",
    content: "您好！我是AI选课助手，有什么可以帮您的吗？"
  });
  scrollToBottom();
});

const handleSendMessage = async () => {
  if (!userInput.value.trim()) {
    ElMessage.warning("请输入消息内容");
    return;
  }

  const userMessage = userInput.value;
  chatMessages.value.push({
    role: "user",
    content: userMessage
  });
  userInput.value = "";
  loading.value = true;
  scrollToBottom();

  try {
    const response = await axios.post("/api/ai/chat", {
      message: userMessage
    });

    chatMessages.value.push({
      role: "assistant",
      content: response.data
    });
  } catch (error) {
    console.error("AI聊天错误:", error);
    ElMessage.error("与AI助手通信时出错");
    chatMessages.value.push({
      role: "assistant",
      content: "抱歉，我暂时无法处理您的请求，请稍后再试。"
    });
  } finally {
    loading.value = false;
    scrollToBottom();
  }
};

const onKeyUp = (event: KeyboardEvent) => {
  if (event.ctrlKey && event.key === "Enter") {
    handleSendMessage();
  }
};

const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
    }
  });
};

const formatMessage = (text: string) => {
  // 简单的格式化，可以将换行符转换为<br>
  return text.replace(/\n/g, "<br>");
};
</script>

<style scoped>
.ai-chat-page {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.ai-chat-container {
  display: flex;
  flex-direction: column;
  height: calc(80vh - 150px);

  .chat-messages {
    flex: 1;
    overflow-y: auto;
    padding: 10px;
    margin-bottom: 16px;
    border: 1px solid #f0f0f0;
    border-radius: 4px;
    background-color: #fff;

    .message {
      margin-bottom: 16px;

      &.user {
        .message-content {
          justify-content: flex-end;
        }

        .message-text {
          background-color: #e6f7ff;
          margin-left: 10px;
        }
      }

      &.assistant {
        .message-content {
          justify-content: flex-start;
        }

        .message-text {
          background-color: #f5f5f5;
          margin-right: 10px;
        }
      }

      .message-content {
        display: flex;
        align-items: flex-start;
      }

      .message-text {
        padding: 8px 12px;
        border-radius: 4px;
        max-width: 70%;
        word-wrap: break-word;
      }
    }
  }

  .chat-input {
    .input-actions {
      display: flex;
      justify-content: flex-end;
      margin-top: 10px;
    }
  }
}
</style>
