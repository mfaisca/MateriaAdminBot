package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Lightning extends _Default{
	public Lightning() {
		super("Lightning", "light", "light", "light", "light");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{4542};
				break;
			case HP:
				ids = new int[]{7877, 7878};
				break;
			case S1:
				ids = new int[]{4534};
				break;
			case S2:
				ids = new int[]{4540};
				break;
			case EX:
				ids = new int[]{7560, 7559};
				break;
			case AA:
				ids = new int[]{7572};
				break;
			case LD:
				ids = new int[]{9624};
				break;
			case BT:
				ids = new int[]{9625};
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