package com.library.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.library.dao.FineDao;
import com.library.pojo.Fine;
import com.library.service.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class FineServiceImpl implements FineService {

    @Autowired
    private FineDao fineDao;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public Fine get(Integer id) {
        return fineDao.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<Fine> selectAll(Integer currentPage, Integer pageSize) {
        List<Fine> list = null;
        if(redisTemplate.opsForValue().get("fineList") != null){
            list = (List<Fine>) redisTemplate.opsForValue().get("fineList");
        }else{
            PageHelper.startPage(currentPage, pageSize);
            list = fineDao.selectAll();
            redisTemplate.opsForValue().set("fineList",list);
        }
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<Fine> selectAllByCondition(Integer currentPage, Integer pageSize, Fine fine) {
        return null;
    }

    @Override
    public int save(Fine fine) {
        return fineDao.insert(fine);
    }

    @Override
    public int update(Fine fine) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = simpleDateFormat.format(new Date());
        return fineDao.updateByPrimaryKeySelective(fine.setPayDate(date));
    }

    @Override
    public int delete(Integer id) {
        return fineDao.deleteByPrimaryKey(id);
    }
}
