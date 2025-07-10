package com.example.manga;

public class ApiResponse {

    private String status;
    private String message;

    public ApiResponse() {
    }


    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    // Getter cho status
    public String getStatus() {
        return status;
    }

    // Setter nếu cần
    public void setStatus(String status) {
        this.status = status;
    }

    // Getter cho message
    public String getMessage() {
        return message;
    }

    // Setter nếu cần
    public void setMessage(String message) {
        this.message = message;
    }

    // Hữu ích để debug/log
    @Override
    public String toString() {
        return "ApiResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}