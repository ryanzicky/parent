package com.zr.encrypt.entity;

import com.baomidou.mybatisplus.mapper.Wrapper;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author zhourui
 * @Date 2021/10/12 14:29
 */
public class EntityWrapper<T> extends Wrapper<T> {
    protected T entity = null;

    public EntityWrapper() {
    }

    public EntityWrapper(T entity) {
        this.entity = entity;
    }

    public EntityWrapper(T entity, String sqlSelect) {
        this.entity = entity;
        this.sqlSelect = sqlSelect;
    }

    public T getEntity() {
        return this.entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public String getSqlSegment() {
        String sqlWhere = this.sql.toString();
        if (StringUtils.isEmpty(sqlWhere)) {
            return null;
        } else {
            return this.isWhere != null ? (this.isWhere ? sqlWhere : sqlWhere.replaceFirst("WHERE", this.AND_OR)) : sqlWhere.replaceFirst("WHERE", this.AND_OR);
        }
    }
}
