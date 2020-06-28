package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Yshtola extends _Default{
	public Yshtola() {
		super("Y'shtola", "yshtola", "yshtola", "yshtola", "yshtola", "yshtola", "yshtola", "yshtola", "yshtola", "yshtola", "yshtola", "yshtola", "yshtola", "yshtola", "yshtola", "yshtola", "yshtola");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{3489};
				break;
			case HP:
				ids = new int[]{5622};
				break;
			case S1:
				ids = new int[]{3459, 3461};
				break;
			case S2:
				ids = new int[]{3463};
				break;
			case EX:
				ids = new int[]{5621};
				break;
			case AA:
				ids = new int[]{5626};
				break;
			case LD:
				ids = new int[]{};
				break;
			case BT:
				ids = new int[]{};
				break;
			case CA:
				ids = new int[]{};
				break;
		}
		if(ids.length == 0)
			return super.getAbility(type);
		else
			return IntStream.of(ids).boxed().map(i -> this.getSpecificAbility(i)).collect(Collectors.toList());
	}
}