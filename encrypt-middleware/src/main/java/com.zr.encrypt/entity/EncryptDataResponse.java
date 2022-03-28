package com.zr.encrypt.entity;

/**
 * @Author zhourui
 * @Date 2021/10/12 13:16
 */
public class EncryptDataResponse {
    private static final long serialVersionUID = 1L;
    private String encryptId;
    private String mask;
    private String content;

    public EncryptDataResponse() {
    }

    public String getEncryptId() {
        return this.encryptId;
    }

    public String getMask() {
        return this.mask;
    }

    public String getContent() {
        return this.content;
    }

    public void setEncryptId(final String encryptId) {
        this.encryptId = encryptId;
    }

    public void setMask(final String mask) {
        this.mask = mask;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof EncryptDataResponse)) {
            return false;
        } else {
            EncryptDataResponse other = (EncryptDataResponse)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label47: {
                    Object this$encryptId = this.getEncryptId();
                    Object other$encryptId = other.getEncryptId();
                    if (this$encryptId == null) {
                        if (other$encryptId == null) {
                            break label47;
                        }
                    } else if (this$encryptId.equals(other$encryptId)) {
                        break label47;
                    }

                    return false;
                }

                Object this$mask = this.getMask();
                Object other$mask = other.getMask();
                if (this$mask == null) {
                    if (other$mask != null) {
                        return false;
                    }
                } else if (!this$mask.equals(other$mask)) {
                    return false;
                }

                Object this$content = this.getContent();
                Object other$content = other.getContent();
                if (this$content == null) {
                    if (other$content != null) {
                        return false;
                    }
                } else if (!this$content.equals(other$content)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof EncryptDataResponse;
    }

    @Override
    public int hashCode() {
        int result = 1;
        Object $encryptId = this.getEncryptId();
        result = result * 59 + ($encryptId == null ? 43 : $encryptId.hashCode());
        Object $mask = this.getMask();
        result = result * 59 + ($mask == null ? 43 : $mask.hashCode());
        Object $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : $content.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "EncryptDataResponse{" +
                "encryptId='" + encryptId + '\'' +
                ", mask='" + mask + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
