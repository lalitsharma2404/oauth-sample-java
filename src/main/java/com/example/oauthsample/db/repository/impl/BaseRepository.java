package com.example.oauthsample.db.repository.impl;

import com.example.oauthsample.db.domain.IdentityModel;
import com.example.oauthsample.db.repository.Repository;
import com.example.oauthsample.exception.InitializationException;
import com.example.oauthsample.util.DateUtil;
import com.google.common.collect.Lists;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class BaseRepository<E extends IdentityModel> implements Repository<E> {

  protected MongoCollection coll;
  protected Class<E> entityModel;

  @Autowired
  private transient Jongo jongo;

  @PostConstruct
  public void init() {
    try {
      Type t = this.getClass().getGenericSuperclass();
      ParameterizedType pt = (ParameterizedType) t;
      this.entityModel = (Class) pt.getActualTypeArguments()[0];
      this.coll = jongo.getCollection(entityModel.getSimpleName().toLowerCase());
    } catch (Exception e) {
      throw new InitializationException(e.getMessage(), e);
    }
  }

  @Override
  public E findById(String id) {
    return this.coll.findOne("{deleted:#, _id:#}", Boolean.FALSE, new ObjectId(id)).as(this.entityModel);
  }

  @Override
  public List<E> findAll() {
    Iterable<E> items = this.coll.find("{deleted:#}", Boolean.FALSE).as(entityModel);
    return Lists.newArrayList(items);
  }

  @Override
  public Map<String, E> findAllAsMap() {
    return asMap(Lists.newArrayList(this.findAll()));
  }

  @Override
  public List<E> findAllBy(String column, String value) {
    Iterable<E> items = this.coll.find("{deleted:#, " + column + ":#}", Boolean.FALSE, value).as(entityModel);
    return Lists.newArrayList(items);
  }

  @Override
  public E findOneBy(String column, String value) {
    return this.coll.findOne("{deleted:#, " + column + ":#}", Boolean.FALSE, value).as(this.entityModel);
  }


  @Override
  public void delete(E object) {
    this.coll.update("{_id:#}", new ObjectId(((IdentityModel) object).getId())).with("{$set: {deleted:#, modified:#}}", Boolean.TRUE, DateUtil.nowInMillis());
  }

  @Override
  public void delete(String id) {
    this.coll.update("{_id:#}", new ObjectId(id)).with("{$set: {deleted:#, modified:#}}", Boolean.TRUE, DateUtil.nowInMillis());
  }

  @Override
  public E save(E e) {
    e.setUuid(UUID.randomUUID().toString());
    e.setCreatedDate(DateUtil.nowInMillis());
    this.coll.insert(e);
    return e;
  }

  @Override
  public E update(E e) {
    e.setUpdatedDate(DateUtil.nowInMillis());
    this.coll.update(new ObjectId(e.getId())).with(e);
    return e;
  }

  @Override
  public Map<String, E> asMap(Iterable<E> items) {
    Map<String, E> map = new HashMap<>();
    for (E item : items) {
      map.put((item).getId(), item);
    }
    return map;
  }

  @Override
  public E findIncludingDeletedById(String id) {
    return this.coll.findOne("{_id:#}", new ObjectId(id)).as(this.entityModel);
  }
}
