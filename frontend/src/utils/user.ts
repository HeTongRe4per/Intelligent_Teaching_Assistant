export const userGlobal = JSON.parse(localStorage.getItem("user") || "{}");
export const usernameGlobal = JSON.parse(localStorage.getItem("user") || "{}")?.username;
export const isNormalGlobal = JSON.parse(localStorage.getItem("user") || "{}")?.role === "normal";
export const isTeacherGlobal = JSON.parse(localStorage.getItem("user") || "{}")?.role === "teacher";
export const isAdminGlobal = JSON.parse(localStorage.getItem("user") || "{}")?.role === "admin";
