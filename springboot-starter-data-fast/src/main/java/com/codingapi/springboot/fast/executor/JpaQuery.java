package com.codingapi.springboot.fast.executor;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.util.Set;

@Slf4j
public class JpaQuery {
    private final Object[] args;
    private final Query query;
    private final String hql;
    private final String countHql;

    private final EntityManager entityManager;

    public JpaQuery(String hql,String countHql,Object[] args, EntityManager entityManager) {
        this.hql = hql;
        this.countHql = countHql;
        this.args = args;
        this.entityManager = entityManager;
        this.query = entityManager.createQuery(hql);
        this.setParameter(query);
    }

    @SneakyThrows
    private void setParameter(Query query){
        if(args!=null&&args.length>0) {
            Set<Parameter<?>> parameters = query.getParameters();
            for(Parameter<?> parameter:parameters){
                Integer position = parameter.getPosition();
                if(position!=null){
                    query.setParameter(position, args[position - 1]);
                }
                if(StringUtils.hasText(parameter.getName())){
                    String name = parameter.getName();
                    Object obj = args[0];
                    Field field =  ReflectionUtils.findField(obj.getClass(),name);
                    if(field!=null) {
                        field.setAccessible(true);
                        query.setParameter(name, field.get(obj));
                    }
                }
            }

        }
    }

    private boolean isPageable() {
        if(args!=null&& args.length>0){
            Object lastObj = args[args.length-1];
            return lastObj instanceof Pageable;
        }
        return false;
    }

    private Pageable getPageable(){
        if(args!=null&& args.length>0){
            Object lastObj = args[args.length-1];
            if(lastObj instanceof Pageable){
                return (Pageable) lastObj;
            }
        }
        return null;
    }

    public Object getResultList() {
        if(isPageable()&&StringUtils.hasText(countHql)){
            Pageable pageable = getPageable();
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
            long total = getCount();
            return new PageImpl<>(query.getResultList(),pageable,total);
        }
        return query.getResultList();
    }


    private long getCount(){
        Query countQuery = entityManager.createQuery(countHql);
        setParameter(countQuery);
        return (Long) countQuery.getSingleResult();
    }


    public Object getSingleResult(){
        return query.getSingleResult();
    }


}
