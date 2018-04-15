package com.ey.tax.core.dao;

import com.ey.tax.model.CommentModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkFlowDAO {
    List<CommentModel> findCommentsByProcInstId(String procInstId);
}
