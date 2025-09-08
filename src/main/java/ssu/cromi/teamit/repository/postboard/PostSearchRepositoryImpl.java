package ssu.cromi.teamit.repository.postboard;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ssu.cromi.teamit.entity.enums.PostCategory;
import ssu.cromi.teamit.entity.postboard.Post;
import ssu.cromi.teamit.entity.postboard.QPost;
import ssu.cromi.teamit.domain.QUser;
import java.util.List;

@RequiredArgsConstructor
public class PostSearchRepositoryImpl implements PostSearchRepository{

    private final JPAQueryFactory queryFactory;
    private final QPost post = QPost.post;
    private final QUser user = QUser.user;

    @Override
    public Page<Post> searchPosts(PostCategory category, String keyword, Pageable pageable) {
        List<Post> content = queryFactory
                .selectFrom(post)
                .where(
                        categoryEq(category),
                        searchCondition(keyword)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.createdAt.desc()) // 최신순
                .fetch();

        Long total = queryFactory
                .select(post.count())
                .from(post)
                .where(
                        categoryEq(category),
                        searchCondition(keyword)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }

    private BooleanExpression categoryEq(PostCategory category) {
        return category != null ? post.category.eq(category) : null;
    }

    private BooleanExpression searchCondition(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return null;
        }

        String processedKeyword = keyword.replaceAll("\\s+", "");

        // 제목(띄어쓰기 무시) OR 내용(띄어쓰기 무시) OR 작성자 닉네임
        return Expressions.stringTemplate("function('REPLACE', {0}, ' ', '')", post.title)
                .containsIgnoreCase(processedKeyword)
                .or(Expressions.stringTemplate("function('REPLACE', {0}, ' ', '')", post.content)
                        .containsIgnoreCase(processedKeyword))
                .or(user.nickName.containsIgnoreCase(keyword)); // 닉네임은 띄어쓰기 없이 검색
    }
}